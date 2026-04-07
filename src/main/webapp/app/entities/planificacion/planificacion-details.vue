<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="planificacion">
        <h2 class="jh-entity-heading" data-cy="planificacionDetailsHeading">
          <span>{{ t$('celupazmasterApp.planificacion.detail.title') }}Planificacion</span> {{ planificacion.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.planificacion.fecha') }}</span>
          </dt>
          <dd>
            <span>{{ planificacion.fecha }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planificacion.almahistory') }}</span>
          </dt>
          <dd>
            <div v-if="planificacion.almahistory">
              <router-link :to="{ name: 'AlmaHistoryView', params: { almaHistoryId: planificacion.almahistory.id } }">{{
                planificacion.almahistory.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planificacion.privilege') }}</span>
          </dt>
          <dd>
            <div v-if="planificacion.privilege">
              <router-link :to="{ name: 'PrivilegeView', params: { privilegeId: planificacion.privilege.id } }">{{
                planificacion.privilege.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.planificacion.planiMaster') }}</span>
          </dt>
          <dd>
            <div v-if="planificacion.planiMaster">
              <router-link :to="{ name: 'PlaniMasterView', params: { planiMasterId: planificacion.planiMaster.id } }">{{
                planificacion.planiMaster.fecha
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="planificacion.id"
          :to="{ name: 'PlanificacionEdit', params: { planificacionId: planificacion.id } }"
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

<script lang="ts" src="./planificacion-details.component.ts"></script>
