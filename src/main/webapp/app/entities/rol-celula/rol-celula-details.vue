<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="rolCelula">
        <h2 class="jh-entity-heading" data-cy="rolCelulaDetailsHeading">
          <span>{{ t$('celupazmasterApp.rolCelula.detail.title') }}RolCelula</span> {{ rolCelula.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.rolCelula.name') }}</span>
          </dt>
          <dd>
            <span>{{ rolCelula.name }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="rolCelula.id"
          :to="{ name: 'RolCelulaEdit', params: { rolCelulaId: rolCelula.id } }"
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

<script lang="ts" src="./rol-celula-details.component.ts"></script>
