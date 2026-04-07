<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="planiMaster">
        <h2 class="jh-entity-heading" data-cy="planiMasterDetailsHeading">
          <span>{{ t$('celupazmasterApp.planiMaster.detail.title') }}PlaniMaster</span> {{ planiMaster.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.fecha') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.fecha }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.ofrenda') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.ofrenda }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.visitaCordinador') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.visitaCordinador }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.visitaTutor') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.visitaTutor }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.visitaDirector') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.visitaDirector }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.otraVisita') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.otraVisita }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.note') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.note }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.doneby') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.doneby }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planiMaster.completado') }}</span>
          </dt>
          <dd>
            <span>{{ planiMaster.completado }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="planiMaster.id"
          :to="{ name: 'PlaniMasterEdit', params: { planiMasterId: planiMaster.id } }"
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

<script lang="ts" src="./plani-master-details.component.ts"></script>
