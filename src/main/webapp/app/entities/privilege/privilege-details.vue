<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="privilege">
        <h2 class="jh-entity-heading" data-cy="privilegeDetailsHeading">
          <span>{{ t$('celupazmasterApp.privilege.detail.title') }}Privilege</span> {{ privilege.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.privilege.name') }}</span>
          </dt>
          <dd>
            <span>{{ privilege.name }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="privilege.id"
          :to="{ name: 'PrivilegeEdit', params: { privilegeId: privilege.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./privilege-details.component.ts"></script>
