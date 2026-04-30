<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.planificacion.home.createOrEditLabel" data-cy="PlanificacionCreateUpdateHeading">
          {{ t$('celupazmasterApp.planificacion.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="planificacion.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="planificacion.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="planificacion">{{ t$('celupazmasterApp.planificacion.fecha') }}</label>
            <template>
              <b-input-group class="mb-3">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="planificacion-fecha"
                    v-model="v$.fecha.$model"
                    name="fecha"
                    class="form-control"
                    :locale="currentLanguage"
                    button-only
                    today-button
                    reset-button
                    close-button
                  >
                  </b-form-datepicker>
                </b-input-group-prepend>
                <b-form-input
                  id="planificacion-fecha"
                  data-cy="fecha"
                  type="date"
                  class="form-control"
                  name="fecha"
                  :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
                  v-model="v$.fecha.$model"
                  required
                />
              </b-input-group>
            </template>
            <span id="planificacion-fecha" class="form-control-plaintext font-weight-bold">
              {{ v$.fecha.$model }}
            </span>
            <div v-if="v$.fecha.$anyDirty && v$.fecha.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fecha.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="planificacion">{{ t$('celupazmasterApp.planificacion.almahistory') }}</label>
            <select
              class="form-control"
              id="planificacion-almahistory"
              data-cy="almahistory"
              name="almahistory"
              v-model="planificacion.almahistory"
            >
              <option :value="null"></option>
              <option
                :value="
                  planificacion.almahistory && almaHistoryOption.id === planificacion.almahistory.id
                    ? planificacion.almahistory
                    : almaHistoryOption
                "
                v-for="almaHistoryOption in almaHistories"
                :key="almaHistoryOption.id"
              >
                {{ almaHistoryOption.alma.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="planificacion">{{ t$('celupazmasterApp.planificacion.privilege') }}</label>
            <select
              class="form-control"
              id="planificacion-privilege"
              data-cy="privilege"
              name="privilege"
              v-model="planificacion.privilege"
            >
              <option :value="null"></option>
              <option
                :value="
                  planificacion.privilege && privilegeOption.id === planificacion.privilege.id ? planificacion.privilege : privilegeOption
                "
                v-for="privilegeOption in privileges"
                :key="privilegeOption.id"
              >
                {{ privilegeOption.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="planificacion">{{ t$('celupazmasterApp.planificacion.planiMaster') }}</label>
            <select
              class="form-control"
              id="planificacion-planiMaster"
              data-cy="planiMaster"
              name="planiMaster"
              v-model="planificacion.planiMaster"
            >
              <option :value="null"></option>
              <option
                :value="
                  planificacion.planiMaster && planiMasterOption.id === planificacion.planiMaster.id
                    ? planificacion.planiMaster
                    : planiMasterOption
                "
                v-for="planiMasterOption in planiMasters"
                :key="planiMasterOption.id"
              >
                {{ planiMasterOption.fecha }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./planificacion-update.component.ts"></script>
