<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="cell">
        <h2 class="jh-entity-heading" data-cy="cellDetailsHeading">
          <span>{{ t$('celupazmasterApp.cell.detail.title') }}Cell</span> {{ cell.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.cell.name') }}</span>
          </dt>
          <dd>
            <span>{{ cell.name }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.startDate') }}</span>
          </dt>
          <dd>
            <span>{{ cell.startDate }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.description') }}</span>
          </dt>
          <dd>
            <span>{{ cell.description }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.sector') }}</span>
          </dt>
          <dd>
            <span>{{ cell.sector }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.lider') }}</span>
          </dt>
          <dd>
            <span>{{ cell.lider }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.cordinador') }}</span>
          </dt>
          <dd>
            <span>{{ cell.cordinador }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.cell.cellType') }}</span>
          </dt>
          <dd>
            <div v-if="cell.cellType">
              <router-link :to="{ name: 'CellTypeView', params: { cellTypeId: cell.cellType.id } }">{{ cell.cellType.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="cell.id" :to="{ name: 'CellEdit', params: { cellId: cell.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cell-details.component.ts"></script>
