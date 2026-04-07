<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.cell.home.createOrEditLabel" data-cy="CellCreateUpdateHeading">
          {{ t$('celupazmasterApp.cell.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="cell.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cell.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.name') }}</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="cell-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.startDate') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="cell-startDate"
                  v-model="v$.startDate.$model"
                  name="startDate"
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
                id="cell-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                v-model="v$.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.description') }}</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="cell-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.sector') }}</label>
            <input
              type="number"
              class="form-control"
              name="sector"
              id="cell-sector"
              data-cy="sector"
              :class="{ valid: !v$.sector.$invalid, invalid: v$.sector.$invalid }"
              v-model.number="v$.sector.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.lider') }}</label>
            <input
              type="text"
              class="form-control"
              name="lider"
              id="cell-lider"
              data-cy="lider"
              :class="{ valid: !v$.lider.$invalid, invalid: v$.lider.$invalid }"
              v-model="v$.lider.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.cordinador') }}</label>
            <input
              type="text"
              class="form-control"
              name="cordinador"
              id="cell-cordinador"
              data-cy="cordinador"
              :class="{ valid: !v$.cordinador.$invalid, invalid: v$.cordinador.$invalid }"
              v-model="v$.cordinador.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="cell">{{ t$('celupazmasterApp.cell.cellType') }}</label>
            <select class="form-control" id="cell-cellType" data-cy="cellType" name="cellType" v-model="cell.cellType">
              <option :value="null"></option>
              <option
                :value="cell.cellType && cellTypeOption.id === cell.cellType.id ? cell.cellType : cellTypeOption"
                v-for="cellTypeOption in cellTypes"
                :key="cellTypeOption.id"
              >
                {{ cellTypeOption.name }}
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
<script lang="ts" src="./cell-update.component.ts"></script>
