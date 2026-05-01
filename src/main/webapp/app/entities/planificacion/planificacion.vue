<template>
  <div>
    <h2 id="page-heading" data-cy="PlanificacionHeading">
      <span id="planificacion">{{ t$('celupazmasterApp.planificacion.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.planificacion.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'PlanificacionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-planificacion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.planificacion.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && planificacions?.length === 0">
      <span>{{ t$('celupazmasterApp.planificacion.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="planificacions?.length > 0">
      <table class="table table-striped" aria-describedby="planificacions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fecha')">
              <span>{{ t$('celupazmasterApp.planificacion.fecha') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('almahistory.id')">
              <span>{{ t$('celupazmasterApp.planificacion.almahistory') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'almahistory.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('privilege.name')">
              <span>{{ t$('celupazmasterApp.planificacion.privilege') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'privilege.name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('planiMaster.fecha')">
              <span>{{ t$('celupazmasterApp.planificacion.planiMaster') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'planiMaster.fecha'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="planificacion in planificacions" :key="planificacion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlanificacionView', params: { planificacionId: planificacion.id } }">{{
                planificacion.id
              }}</router-link>
            </td>
            <td>{{ planificacion.fecha }}</td>
            <td>
              <div v-if="planificacion.almahistory">
                <router-link :to="{ name: 'AlmaHistoryView', params: { almaHistoryId: planificacion.almahistory.id } }">{{
                  planificacion.almahistory.alma.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="planificacion.privilege">
                <router-link :to="{ name: 'PrivilegeView', params: { privilegeId: planificacion.privilege.id } }">{{
                  planificacion.privilege.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="planificacion.planiMaster">
                <router-link :to="{ name: 'PlaniMasterView', params: { planiMasterId: planificacion.planiMaster.id } }">{{
                  planificacion.planiMaster.fecha
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PlanificacionView', params: { planificacionId: planificacion.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PlanificacionEdit', params: { planificacionId: planificacion.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(planificacion)"
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
        <span id="celupazmasterApp.planificacion.delete.question" data-cy="planificacionDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-planificacion-heading">{{ t$('celupazmasterApp.planificacion.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-planificacion"
            data-cy="entityConfirmDeleteButton"
            @click="removePlanificacion"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="planificacions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./planificacion.component.ts"></script>
