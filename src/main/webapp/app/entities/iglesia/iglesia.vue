<template>
  <div>
    <h2 id="page-heading" data-cy="IglesiaHeading">
      <span id="iglesia">{{ t$('celupazmasterApp.iglesia.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.iglesia.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'IglesiaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-iglesia"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.iglesia.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && iglesias?.length === 0">
      <span>{{ t$('celupazmasterApp.iglesia.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="iglesias?.length > 0">
      <table class="table table-striped" aria-describedby="iglesias">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('celupazmasterApp.iglesia.name') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="iglesia in iglesias" :key="iglesia.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'IglesiaView', params: { iglesiaId: iglesia.id } }">{{ iglesia.id }}</router-link>
            </td>
            <td>{{ iglesia.name }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'IglesiaView', params: { iglesiaId: iglesia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'IglesiaEdit', params: { iglesiaId: iglesia.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(iglesia)"
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
        <span id="celupazmasterApp.iglesia.delete.question" data-cy="iglesiaDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-iglesia-heading">{{ t$('celupazmasterApp.iglesia.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-iglesia"
            data-cy="entityConfirmDeleteButton"
            @click="removeIglesia"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./iglesia.component.ts"></script>
