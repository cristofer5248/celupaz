<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="alma">
        <h2 class="jh-entity-heading" data-cy="almaDetailsHeading">
          <span>{{ t$('celupazmasterApp.alma.detail.title') }}Alma</span> {{ alma.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('celupazmasterApp.alma.name') }}</span>
          </dt>
          <dd>
            <span>{{ alma.name }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.email') }}</span>
          </dt>
          <dd>
            <span>{{ alma.email }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.phone') }}</span>
          </dt>
          <dd>
            <span>{{ alma.phone }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.department') }}</span>
          </dt>
          <dd>
            <span>{{ alma.department }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.municipality') }}</span>
          </dt>
          <dd>
            <span>{{ alma.municipality }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.colony') }}</span>
          </dt>
          <dd>
            <span>{{ alma.colony }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.description') }}</span>
          </dt>
          <dd>
            <span>{{ alma.description }}</span>
          </dd>
          <dt>
            <span>{{ t$('celupazmasterApp.alma.foto') }}</span>
          </dt>
          <dd>
            <div v-if="alma.foto">
              <a @click="openFile(alma.fotoContentType, alma.foto)">
                <img :src="'data:' + alma.fotoContentType + ';base64,' + alma.foto" style="max-width: 100%" alt="alma" />
              </a>
              {{ alma.fotoContentType }}, {{ byteSize(alma.foto) }}
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="alma.id" :to="{ name: 'AlmaEdit', params: { almaId: alma.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./alma-details.component.ts"></script>
