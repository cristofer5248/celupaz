<template>
  <div>
    <h2 id="page-heading" data-cy="PlaniMasterHeading">
      <span id="plani-master">{{ t$('celupazmasterApp.planiMaster.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.planiMaster.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'PlaniMasterCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-plani-master"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.planiMaster.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && planiMasters?.length === 0">
      <span>{{ t$('celupazmasterApp.planiMaster.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="planiMasters?.length > 0">
      <table class="table table-striped" aria-describedby="planiMasters">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fecha')">
              <span>{{ t$('celupazmasterApp.planiMaster.fecha') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('ofrenda')">
              <span>{{ t$('celupazmasterApp.planiMaster.ofrenda') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'ofrenda'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('visitaCordinador')">
              <span>{{ t$('celupazmasterApp.planiMaster.visitaCordinador') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'visitaCordinador'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('visitaTutor')">
              <span>{{ t$('celupazmasterApp.planiMaster.visitaTutor') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'visitaTutor'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('visitaDirector')">
              <span>{{ t$('celupazmasterApp.planiMaster.visitaDirector') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'visitaDirector'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('otraVisita')">
              <span>{{ t$('celupazmasterApp.planiMaster.otraVisita') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'otraVisita'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('note')">
              <span>{{ t$('celupazmasterApp.planiMaster.note') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'note'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('doneby')">
              <span>{{ t$('celupazmasterApp.planiMaster.doneby') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'doneby'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('completado')">
              <span>{{ t$('celupazmasterApp.planiMaster.completado') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'completado'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="planiMaster in planiMasters" :key="planiMaster.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlaniMasterView', params: { planiMasterId: planiMaster.id } }">{{ planiMaster.id }}</router-link>
            </td>
            <td>{{ planiMaster.fecha }}</td>
            <td>{{ planiMaster.ofrenda }}</td>
            <td>{{ planiMaster.visitaCordinador }}</td>
            <td>{{ planiMaster.visitaTutor }}</td>
            <td>{{ planiMaster.visitaDirector }}</td>
            <td>{{ planiMaster.otraVisita }}</td>
            <td>{{ planiMaster.note }}</td>
            <td>{{ planiMaster.doneby }}</td>
            <td>{{ planiMaster.completado }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'PlaniMasterView', params: { planiMasterId: planiMaster.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PlaniMasterEdit', params: { planiMasterId: planiMaster.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(planiMaster)"
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
        <span id="celupazmasterApp.planiMaster.delete.question" data-cy="planiMasterDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-planiMaster-heading">{{ t$('celupazmasterApp.planiMaster.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-planiMaster"
            data-cy="entityConfirmDeleteButton"
            @click="removePlaniMaster"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="planiMasters?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plani-master.component.ts"></script>
