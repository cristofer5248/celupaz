<template>
  <div>
    <h2 id="page-heading" data-cy="AttendanceHeading">
      <span id="attendance">{{ t$('celupazmasterApp.attendance.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.attendance.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AttendanceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-attendance"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.attendance.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && attendances?.length === 0">
      <span>{{ t$('celupazmasterApp.attendance.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="attendances?.length > 0">
      <table class="table table-striped" aria-describedby="attendances">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fecha')">
              <span>{{ t$('celupazmasterApp.attendance.fecha') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fecha'"></jhi-sort-indicator>
            </th>
            <th scope="col">
              <span>Miembro</span>
            </th>
            <th scope="col">
              <span>Célula</span>
            </th>
            <th scope="col">
              <span>Estado</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="attendance in attendances" :key="attendance.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AttendanceView', params: { attendanceId: attendance.id } }">
                {{ attendance.id }}
              </router-link>
            </td>
            <td>{{ attendance.fecha }}</td>

            <td>{{ attendance.memberName }}</td>

            <td>{{ attendance.cellName }}</td>

            <td>
              <span :class="['badge', attendance.enabled ? 'bg-success' : 'bg-danger']">
                {{ attendance.enabled ? 'Activo' : 'Inactivo' }}
              </span>
            </td>

            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'AttendanceView', params: { attendanceId: attendance.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AttendanceEdit', params: { attendanceId: attendance.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(attendance)"
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
        <span id="celupazmasterApp.attendance.delete.question" data-cy="attendanceDeleteDialogHeading">
          {{ t$('entity.delete.title') }}
        </span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-attendance-heading">{{ t$('celupazmasterApp.attendance.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-attendance"
            data-cy="entityConfirmDeleteButton"
            @click="removeAttendance"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>

    <div v-show="attendances?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./attendance.component.ts"></script>
