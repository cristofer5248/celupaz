<template>
  <div>
    <h2 id="page-heading" data-cy="CellHeading">
      <span id="cell">{{ t$('celupazmasterApp.cell.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.cell.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'CellCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-cell">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.cell.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cells?.length === 0">
      <span>{{ t$('celupazmasterApp.cell.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="cells?.length > 0">
      <table class="table table-striped" aria-describedby="cells">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('name')">
              <span>{{ t$('celupazmasterApp.cell.name') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('startDate')">
              <span>{{ t$('celupazmasterApp.cell.startDate') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ t$('celupazmasterApp.cell.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sector')">
              <span>{{ t$('celupazmasterApp.cell.sector') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sector'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('lider')">
              <span>{{ t$('celupazmasterApp.cell.lider') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lider'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cordinador')">
              <span>{{ t$('celupazmasterApp.cell.cordinador') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cordinador'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cellType.name')">
              <span>{{ t$('celupazmasterApp.cell.cellType') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cellType.name'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cell in cells" :key="cell.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CellView', params: { cellId: cell.id } }">{{ cell.id }}</router-link>
            </td>
            <td>{{ cell.name }}</td>
            <td>{{ cell.startDate }}</td>
            <td>{{ cell.description }}</td>
            <td>{{ cell.sector }}</td>
            <td>{{ cell.lider }}</td>
            <td>{{ cell.cordinador }}</td>
            <td>
              <div v-if="cell.cellType">
                <router-link :to="{ name: 'CellTypeView', params: { cellTypeId: cell.cellType.id } }">{{ cell.cellType.name }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'CellView', params: { cellId: cell.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CellEdit', params: { cellId: cell.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cell)"
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
        <span id="celupazmasterApp.cell.delete.question" data-cy="cellDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cell-heading">{{ t$('celupazmasterApp.cell.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cell"
            data-cy="entityConfirmDeleteButton"
            @click="removeCell"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="cells?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cell.component.ts"></script>
