<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="memberCelula">
        <h2 class="jh-entity-heading" data-cy="memberCelulaDetailsHeading">
          <span>{{ t$('celupazmasterApp.memberCelula.detail.title') }}MemberCelula</span> {{ memberCelula.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.memberCelula.fechaCreada') }}</span>
          </dt>
          <dd>
            <span>{{ memberCelula.fechaCreada }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.memberCelula.enabled') }}</span>
          </dt>
          <dd>
            <span>{{ memberCelula.enabled }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.memberCelula.member') }}</span>
          </dt>
          <dd>
            <div v-if="memberCelula.member">
              <router-link :to="{ name: 'MemberView', params: { memberId: memberCelula.member.id } }">{{
                memberCelula.member.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.memberCelula.cell') }}</span>
          </dt>
          <dd>
            <div v-if="memberCelula.cell">
              <router-link :to="{ name: 'CellView', params: { cellId: memberCelula.cell.id } }">{{ memberCelula.cell.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="memberCelula.id"
          :to="{ name: 'MemberCelulaEdit', params: { memberCelulaId: memberCelula.id } }"
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

<script lang="ts" src="./member-celula-details.component.ts"></script>
