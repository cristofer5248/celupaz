<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="almaHistory">
        <h2 class="jh-entity-heading" data-cy="almaHistoryDetailsHeading">
          <span>{{ t$('celupazmasterApp.almaHistory.detail.title') }}AlmaHistory</span> {{ almaHistory.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.almaHistory.fecha') }}</span>
          </dt>
          <dd>
            <span>{{ almaHistory.fecha }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.almaHistory.alma') }}</span>
          </dt>
          <dd>
            <div v-if="almaHistory.alma">
              <router-link :to="{ name: 'AlmaView', params: { almaId: almaHistory.alma.id } }">{{ almaHistory.alma.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.almaHistory.cell') }}</span>
          </dt>
          <dd>
            <div v-if="almaHistory.cell">
              <router-link :to="{ name: 'CellView', params: { cellId: almaHistory.cell.id } }">{{ almaHistory.cell.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.almaHistory.rolcelula') }}</span>
          </dt>
          <dd>
            <div v-if="almaHistory.rolcelula">
              <router-link :to="{ name: 'RolCelulaView', params: { rolCelulaId: almaHistory.rolcelula.id } }">{{
                almaHistory.rolcelula.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="almaHistory.id"
          :to="{ name: 'AlmaHistoryEdit', params: { almaHistoryId: almaHistory.id } }"
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

<script lang="ts" src="./alma-history-details.component.ts"></script>
