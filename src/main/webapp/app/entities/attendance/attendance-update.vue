<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.attendance.home.createOrEditLabel" data-cy="AttendanceCreateUpdateHeading">
          {{ t$('celupazmasterApp.attendance.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="attendance.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attendance.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="attendance">{{ t$('celupazmasterApp.attendance.fecha') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="attendance-fecha"
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
                id="attendance-fecha"
                data-cy="fecha"
                type="text"
                class="form-control"
                name="fecha"
                :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
                v-model="v$.fecha.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fecha.$anyDirty && v$.fecha.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fecha.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="attendance">{{ t$('celupazmasterApp.attendance.membercelula') }}</label>
            <select
              class="form-control"
              id="attendance-membercelula"
              data-cy="membercelula"
              name="membercelula"
              v-model="attendance.membercelula"
            >
              <option :value="null"></option>
              <option
                :value="
                  attendance.membercelula && memberCelulaOption.id === attendance.membercelula.id
                    ? attendance.membercelula
                    : memberCelulaOption
                "
                v-for="memberCelulaOption in memberCelulas"
                :key="memberCelulaOption.id"
              >
                {{ memberCelulaOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="attendance">{{ t$('celupazmasterApp.attendance.planificacion') }}</label>
            <select
              class="form-control"
              id="attendance-planificacion"
              data-cy="planificacion"
              name="planificacion"
              v-model="attendance.planificacion"
            >
              <option :value="null"></option>
              <option
                :value="
                  attendance.planificacion && planificacionOption.id === attendance.planificacion.id
                    ? attendance.planificacion
                    : planificacionOption
                "
                v-for="planificacionOption in planificacions"
                :key="planificacionOption.id"
              >
                {{ planificacionOption.fecha }}
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
<script lang="ts" src="./attendance-update.component.ts"></script>
