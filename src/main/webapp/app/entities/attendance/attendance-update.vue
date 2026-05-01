<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.attendance.home.createOrEditLabel" data-cy="AttendanceCreateUpdateHeading">
          {{ t$('celupazmasterApp.attendance.home.createOrEditLabel') }}
        </h2>

        <!-- EDIT MODE -->
        <div v-if="isEdit">
          <div class="mb-3">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attendance.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label">{{ t$('celupazmasterApp.attendance.fecha') }}</label>
            <span class="form-control-plaintext font-weight-bold">{{ sharedFecha }}</span>
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
                {{ memberCelulaOption.member.name }}
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
                {{ planificacionOption.planiMaster.fecha }}
              </option>
            </select>
          </div>
        </div>

        <!-- CREATE MODE -->
        <div v-else>
          <div class="card mb-4">
            <div class="card-body">
              <h5 class="card-title">Datos Comunes</h5>
              <div class="mb-3">
                <label class="form-control-label">{{ t$('celupazmasterApp.attendance.fecha') }}</label>
                <span class="form-control-plaintext font-weight-bold">{{ sharedFecha }}</span>
              </div>
              <div class="mb-3">
                <label class="form-control-label" for="shared-planificacion">{{ t$('celupazmasterApp.attendance.planificacion') }}</label>
                <select class="form-control" id="shared-planificacion" name="sharedPlanificacion" v-model="sharedPlanificacion" required>
                  <option :value="null">Seleccione una planificación...</option>
                  <option :value="planificacionOption" v-for="planificacionOption in planificacions" :key="planificacionOption.id">
                    {{ planificacionOption.planiMaster.fecha }}
                  </option>
                </select>
              </div>
            </div>
          </div>

          <div class="card mb-4">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="card-title mb-0">Asistentes</h5>
                <button type="button" class="btn btn-success btn-sm" @click="addItem()">
                  <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>Agregar</span>
                </button>
              </div>

              <div v-for="(item, index) in items" :key="index" class="row align-items-end mb-3 pb-3 border-bottom">
                <div class="col-md-10">
                  <label class="form-control-label">{{ t$('celupazmasterApp.attendance.membercelula') }}</label>
                  <select class="form-control" v-model="item.membercelula" required>
                    <option :value="null">Seleccione un miembro...</option>
                    <option
                      :value="memberCelulaOption"
                      v-for="memberCelulaOption in getAvailableMemberCelulas(index)"
                      :key="memberCelulaOption.id"
                    >
                      {{ memberCelulaOption.member.name }}
                    </option>
                    <!-- Also include currently selected one to show it -->
                    <option
                      v-if="item.membercelula && !getAvailableMemberCelulas(index).find(mc => mc.id === item.membercelula.id)"
                      :value="item.membercelula"
                    >
                      {{ item.membercelula.member.name }}
                    </option>
                  </select>
                </div>
                <div class="col-md-2">
                  <button type="button" class="btn btn-danger w-100" @click="removeItem(index)" :disabled="items.length === 1">
                    <font-awesome-icon icon="trash"></font-awesome-icon>&nbsp;<span>Quitar</span>
                  </button>
                </div>
              </div>
            </div>
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
            :disabled="!isFormValid || isSaving"
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
