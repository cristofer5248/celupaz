<template>
  <div>
    <h2 id="page-heading" data-cy="MemberCelulaHeading">
      <span id="member-celula">{{ t$('celupazmasterApp.memberCelula.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('celupazmasterApp.memberCelula.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'MemberCelulaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-member-celula"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('celupazmasterApp.memberCelula.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && memberCelulas?.length === 0">
      <span>{{ t$('celupazmasterApp.memberCelula.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="memberCelulas?.length > 0">
      <table class="table table-striped" aria-describedby="memberCelulas">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('celupazmasterApp.memberCelula.fechaCreada') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('celupazmasterApp.memberCelula.enabled') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('celupazmasterApp.memberCelula.member') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('celupazmasterApp.memberCelula.cell') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="memberCelula in memberCelulas" :key="memberCelula.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MemberCelulaView', params: { memberCelulaId: memberCelula.id } }">{{
                memberCelula.id
              }}</router-link>
            </td>
            <td>{{ memberCelula.fechaCreada }}</td>
            <td>{{ memberCelula.enabled }}</td>
            <td>
              <div v-if="memberCelula.member">
                <router-link :to="{ name: 'MemberView', params: { memberId: memberCelula.member.id } }">{{
                  memberCelula.member.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="memberCelula.cell">
                <router-link :to="{ name: 'CellView', params: { cellId: memberCelula.cell.id } }">{{ memberCelula.cell.name }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'MemberCelulaView', params: { memberCelulaId: memberCelula.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MemberCelulaEdit', params: { memberCelulaId: memberCelula.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(memberCelula)"
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
        <span id="celupazmasterApp.memberCelula.delete.question" data-cy="memberCelulaDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-memberCelula-heading">{{ t$('celupazmasterApp.memberCelula.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-memberCelula"
            data-cy="entityConfirmDeleteButton"
            @click="removeMemberCelula"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./member-celula.component.ts"></script>
