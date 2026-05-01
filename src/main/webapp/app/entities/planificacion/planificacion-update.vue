<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.planificacion.home.createOrEditLabel" data-cy="PlanificacionCreateUpdateHeading">
          {{ t$('celupazmasterApp.planificacion.home.createOrEditLabel') }}
        </h2>

        <!-- EDIT MODE -->
        <div v-if="isEdit">
          <div class="mb-3">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="planificacion.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label">{{ t$('celupazmasterApp.planificacion.fecha') }}</label>
            <span class="form-control-plaintext font-weight-bold">{{ sharedFecha }}</span>
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

        <!-- CREATE MODE -->
        <div v-else>
          <div class="card mb-4">
            <div class="card-body">
              <h5 class="card-title">Datos Comunes</h5>
              <div class="mb-3">
                <label class="form-control-label">{{ t$('celupazmasterApp.planificacion.fecha') }}</label>
                <span class="form-control-plaintext font-weight-bold">{{ sharedFecha }}</span>
              </div>
              <div class="mb-3">
                <label class="form-control-label" for="shared-planiMaster">{{ t$('celupazmasterApp.planificacion.planiMaster') }}</label>
                <select class="form-control" id="shared-planiMaster" name="sharedPlaniMaster" v-model="sharedPlaniMaster" required>
                  <option :value="null">Seleccione un día de célula...</option>
                  <option :value="planiMasterOption" v-for="planiMasterOption in planiMasters" :key="planiMasterOption.id">
                    {{ planiMasterOption.fecha }}
                  </option>
                </select>
              </div>
            </div>
          </div>

          <div class="card mb-4">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="card-title mb-0">Asignaciones</h5>
                <button type="button" class="btn btn-success btn-sm" @click="addItem()">
                  <font-awesome-icon icon="plus"></font-awesome-icon>&nbsp;<span>Agregar</span>
                </button>
              </div>

              <div v-for="(item, index) in items" :key="index" class="row align-items-end mb-3 pb-3 border-bottom">
                <div class="col-md-5">
                  <label class="form-control-label">{{ t$('celupazmasterApp.planificacion.almahistory') }}</label>
                  <select class="form-control" v-model="item.almahistory" required>
                    <option :value="null">Seleccione equipo de trabajo...</option>
                    <option
                      :value="almaHistoryOption"
                      v-for="almaHistoryOption in getAvailableAlmaHistories(index)"
                      :key="almaHistoryOption.id"
                    >
                      {{ almaHistoryOption.alma.name }}
                    </option>
                    <!-- Also include currently selected one to show it -->
                    <option
                      v-if="item.almahistory && !getAvailableAlmaHistories(index).find(ah => ah.id === item.almahistory.id)"
                      :value="item.almahistory"
                    >
                      {{ item.almahistory.alma.name }}
                    </option>
                  </select>
                </div>
                <div class="col-md-5">
                  <label class="form-control-label">{{ t$('celupazmasterApp.planificacion.privilege') }}</label>
                  <select class="form-control" v-model="item.privilege" required>
                    <option :value="null">Seleccione privilegio...</option>
                    <option :value="privilegeOption" v-for="privilegeOption in privileges" :key="privilegeOption.id">
                      {{ privilegeOption.name }}
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
<script lang="ts" src="./planificacion-update.component.ts"></script>
