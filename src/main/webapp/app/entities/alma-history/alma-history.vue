<template>
  <div>
    <h2 id="page-heading" data-cy="AlmaHistoryHeading">
      <span id="alma-history">{{ t$('celupazmasterApp.almaHistory.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.almaHistory.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AlmaHistoryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-alma-history"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.almaHistory.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && almaHistories?.length === 0">
      <span>{{ t$('celupazmasterApp.almaHistory.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="almaHistories?.length > 0">
      <table class="table table-striped" aria-describedby="almaHistories">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fecha')">
              <span>{{ t$('celupazmasterApp.almaHistory.fecha') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('alma.name')">
              <span>{{ t$('celupazmasterApp.almaHistory.alma') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'alma.name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cell.name')">
              <span>{{ t$('celupazmasterApp.almaHistory.cell') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cell.name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('rolcelula.name')">
              <span>{{ t$('celupazmasterApp.almaHistory.rolcelula') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'rolcelula.name'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="almaHistory in almaHistories" :key="almaHistory.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AlmaHistoryView', params: { almaHistoryId: almaHistory.id } }">{{ almaHistory.id }}</router-link>
            </td>
            <td>{{ almaHistory.fecha }}</td>
            <td>
              <div v-if="almaHistory.alma">
                <router-link :to="{ name: 'AlmaView', params: { almaId: almaHistory.alma.id } }">{{ almaHistory.alma.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="almaHistory.cell">
                <router-link :to="{ name: 'CellView', params: { cellId: almaHistory.cell.id } }">{{ almaHistory.cell.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="almaHistory.rolcelula">
                <router-link :to="{ name: 'RolCelulaView', params: { rolCelulaId: almaHistory.rolcelula.id } }">{{
                  almaHistory.rolcelula.name
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'AlmaHistoryView', params: { almaHistoryId: almaHistory.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AlmaHistoryEdit', params: { almaHistoryId: almaHistory.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(almaHistory)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ t$('entity.action.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="celupazmasterApp.almaHistory.delete.question" data-cy="almaHistoryDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-almaHistory-heading">{{ t$('celupazmasterApp.almaHistory.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-almaHistory"
            data-cy="entityConfirmDeleteButton"
            @click="removeAlmaHistory"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="almaHistories?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alma-history.component.ts"></script>
