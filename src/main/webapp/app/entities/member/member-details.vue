<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="member">
        <h2 class="jh-entity-heading" data-cy="memberDetailsHeading">
          <span>{{ t$('celupazmasterApp.member.detail.title') }}Member</span> {{ member.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.member.name') }}</span>
          </dt>
          <dd>
            <span>{{ member.name }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.email') }}</span>
          </dt>
          <dd>
            <span>{{ member.email }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.phone') }}</span>
          </dt>
          <dd>
            <span>{{ member.phone }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.department') }}</span>
          </dt>
          <dd>
            <span>{{ member.department }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.municipality') }}</span>
          </dt>
          <dd>
            <span>{{ member.municipality }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.colony') }}</span>
          </dt>
          <dd>
            <span>{{ member.colony }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.isCompaz') }}</span>
          </dt>
          <dd>
            <span>{{ member.isCompaz }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.fechacumple') }}</span>
          </dt>
          <dd>
            <span>{{ member.fechacumple }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.padre') }}</span>
          </dt>
          <dd>
            <span>{{ member.padre }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.relacion') }}</span>
          </dt>
          <dd>
            <span>{{ member.relacion }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.member.iglesia') }}</span>
          </dt>
          <dd>
            <div v-if="member.iglesia">
              <router-link :to="{ name: 'IglesiaView', params: { iglesiaId: member.iglesia.id } }">{{ member.iglesia.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="member.id" :to="{ name: 'MemberEdit', params: { memberId: member.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./member-details.component.ts"></script>
