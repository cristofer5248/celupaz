<template>
  <div>
    <h2 id="page-heading" data-cy="AlmaHeading">
      <span id="alma">{{ t$('celupazmasterApp.alma.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.alma.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AlmaCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-alma">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.alma.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && almas?.length === 0">
      <span>{{ t$('celupazmasterApp.alma.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="almas?.length > 0">
      <table class="table table-striped" aria-describedby="almas">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('name')">
              <span>{{ t$('celupazmasterApp.alma.name') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('email')">
              <span>{{ t$('celupazmasterApp.alma.email') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('phone')">
              <span>{{ t$('celupazmasterApp.alma.phone') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('department')">
              <span>{{ t$('celupazmasterApp.alma.department') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'department'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('municipality')">
              <span>{{ t$('celupazmasterApp.alma.municipality') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'municipality'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('colony')">
              <span>{{ t$('celupazmasterApp.alma.colony') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'colony'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ t$('celupazmasterApp.alma.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('foto')">
              <span>{{ t$('celupazmasterApp.alma.foto') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'foto'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alma in almas" :key="alma.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AlmaView', params: { almaId: alma.id } }">{{ alma.id }}</router-link>
            </td>
            <td>{{ alma.name }}</td>
            <td>{{ alma.email }}</td>
            <td>{{ alma.phone }}</td>
            <td>{{ alma.department }}</td>
            <td>{{ alma.municipality }}</td>
            <td>{{ alma.colony }}</td>
            <td>{{ alma.description }}</td>
            <td>
              <a v-if="alma.foto" @click="openFile(alma.fotoContentType, alma.foto)">
                <img :src="'data:' + alma.fotoContentType + ';base64,' + alma.foto" style="max-height: 30px" alt="alma" />
              </a>
              <span v-if="alma.foto">{{ alma.fotoContentType }}, {{ byteSize(alma.foto) }}</span>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'AlmaView', params: { almaId: alma.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AlmaEdit', params: { almaId: alma.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(alma)"
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
        <span id="celupazmasterApp.alma.delete.question" data-cy="almaDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-alma-heading">{{ t$('celupazmasterApp.alma.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-alma"
            data-cy="entityConfirmDeleteButton"
            @click="removeAlma"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="almas?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alma.component.ts"></script>
