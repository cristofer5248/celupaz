<template>
  <div>
    <h2 id="page-heading" data-cy="MemberHeading">
      <span id="member">{{ t$('celupazmasterApp.member.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.member.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'MemberCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-member"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.member.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && members?.length === 0">
      <span>{{ t$('celupazmasterApp.member.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="members?.length > 0">
      <table class="table table-striped" aria-describedby="members">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('name')">
              <span>{{ t$('celupazmasterApp.member.name') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('email')">
              <span>{{ t$('celupazmasterApp.member.email') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'email'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('phone')">
              <span>{{ t$('celupazmasterApp.member.phone') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'phone'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('department')">
              <span>{{ t$('celupazmasterApp.member.department') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'department'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('municipality')">
              <span>{{ t$('celupazmasterApp.member.municipality') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'municipality'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('colony')">
              <span>{{ t$('celupazmasterApp.member.colony') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'colony'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('isCompaz')">
              <span>{{ t$('celupazmasterApp.member.isCompaz') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isCompaz'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('fechacumple')">
              <span>{{ t$('celupazmasterApp.member.fechacumple') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechacumple'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('padre')">
              <span>{{ t$('celupazmasterApp.member.padre') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'padre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('relacion')">
              <span>{{ t$('celupazmasterApp.member.relacion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'relacion'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('iglesia.name')">
              <span>{{ t$('celupazmasterApp.member.iglesia') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'iglesia.name'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in members" :key="member.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MemberView', params: { memberId: member.id } }">{{ member.id }}</router-link>
            </td>
            <td>{{ member.name }}</td>
            <td>{{ member.email }}</td>
            <td>{{ member.phone }}</td>
            <td>{{ member.department }}</td>
            <td>{{ member.municipality }}</td>
            <td>{{ member.colony }}</td>
            <td>{{ member.isCompaz }}</td>
            <td>{{ member.fechacumple }}</td>
            <td>{{ member.padre }}</td>
            <td>{{ member.relacion }}</td>
            <td>
              <div v-if="member.iglesia">
                <router-link :to="{ name: 'IglesiaView', params: { iglesiaId: member.iglesia.id } }">{{ member.iglesia.name }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'MemberView', params: { memberId: member.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MemberEdit', params: { memberId: member.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(member)"
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
        <span id="celupazmasterApp.member.delete.question" data-cy="memberDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-member-heading">{{ t$('celupazmasterApp.member.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-member"
            data-cy="entityConfirmDeleteButton"
            @click="removeMember"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="members?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./member.component.ts"></script>
