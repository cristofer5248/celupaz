<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.planiMaster.home.createOrEditLabel" data-cy="PlaniMasterCreateUpdateHeading">
          {{ t$('celupazmasterApp.planiMaster.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="planiMaster.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="planiMaster.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.fecha') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="plani-master-fecha"
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
                id="plani-master-fecha"
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
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.ofrenda') }}</label>
            <input
              type="number"
              class="form-control"
              name="ofrenda"
              id="plani-master-ofrenda"
              data-cy="ofrenda"
              :class="{ valid: !v$.ofrenda.$invalid, invalid: v$.ofrenda.$invalid }"
              v-model.number="v$.ofrenda.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.visitaCordinador') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="visitaCordinador"
              id="plani-master-visitaCordinador"
              data-cy="visitaCordinador"
              :class="{ valid: !v$.visitaCordinador.$invalid, invalid: v$.visitaCordinador.$invalid }"
              v-model="v$.visitaCordinador.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.visitaTutor') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="visitaTutor"
              id="plani-master-visitaTutor"
              data-cy="visitaTutor"
              :class="{ valid: !v$.visitaTutor.$invalid, invalid: v$.visitaTutor.$invalid }"
              v-model="v$.visitaTutor.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.visitaDirector') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="visitaDirector"
              id="plani-master-visitaDirector"
              data-cy="visitaDirector"
              :class="{ valid: !v$.visitaDirector.$invalid, invalid: v$.visitaDirector.$invalid }"
              v-model="v$.visitaDirector.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.otraVisita') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="otraVisita"
              id="plani-master-otraVisita"
              data-cy="otraVisita"
              :class="{ valid: !v$.otraVisita.$invalid, invalid: v$.otraVisita.$invalid }"
              v-model="v$.otraVisita.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.note') }}</label>
            <input
              type="text"
              class="form-control"
              name="note"
              id="plani-master-note"
              data-cy="note"
              :class="{ valid: !v$.note.$invalid, invalid: v$.note.$invalid }"
              v-model="v$.note.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.doneby') }}</label>
            <input
              type="text"
              class="form-control"
              name="doneby"
              id="plani-master-doneby"
              data-cy="doneby"
              :class="{ valid: !v$.doneby.$invalid, invalid: v$.doneby.$invalid }"
              v-model="v$.doneby.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="plani-master">{{ t$('celupazmasterApp.planiMaster.completado') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="completado"
              id="plani-master-completado"
              data-cy="completado"
              :class="{ valid: !v$.completado.$invalid, invalid: v$.completado.$invalid }"
              v-model="v$.completado.$model"
            />
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
<script lang="ts" src="./plani-master-update.component.ts"></script>
