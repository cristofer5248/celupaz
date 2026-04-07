<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.almaHistory.home.createOrEditLabel" data-cy="AlmaHistoryCreateUpdateHeading">
          {{ t$('celupazmasterApp.almaHistory.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="almaHistory.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="almaHistory.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma-history">{{ t$('celupazmasterApp.almaHistory.fecha') }}</label>
            <input
              type="text"
              class="form-control"
              name="fecha"
              id="alma-history-fecha"
              data-cy="fecha"
              :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
              v-model="v$.fecha.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma-history">{{ t$('celupazmasterApp.almaHistory.alma') }}</label>
            <select class="form-control" id="alma-history-alma" data-cy="alma" name="alma" v-model="almaHistory.alma">
              <option :value="null"></option>
              <option
                :value="almaHistory.alma && almaOption.id === almaHistory.alma.id ? almaHistory.alma : almaOption"
                v-for="almaOption in almas"
                :key="almaOption.id"
              >
                {{ almaOption.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma-history">{{ t$('celupazmasterApp.almaHistory.cell') }}</label>
            <select class="form-control" id="alma-history-cell" data-cy="cell" name="cell" v-model="almaHistory.cell">
              <option :value="null"></option>
              <option
                :value="almaHistory.cell && cellOption.id === almaHistory.cell.id ? almaHistory.cell : cellOption"
                v-for="cellOption in cells"
                :key="cellOption.id"
              >
                {{ cellOption.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma-history">{{ t$('celupazmasterApp.almaHistory.rolcelula') }}</label>
            <select class="form-control" id="alma-history-rolcelula" data-cy="rolcelula" name="rolcelula" v-model="almaHistory.rolcelula">
              <option :value="null"></option>
              <option
                :value="almaHistory.rolcelula && rolCelulaOption.id === almaHistory.rolcelula.id ? almaHistory.rolcelula : rolCelulaOption"
                v-for="rolCelulaOption in rolCelulas"
                :key="rolCelulaOption.id"
              >
                {{ rolCelulaOption.name }}
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
<script lang="ts" src="./alma-history-update.component.ts"></script>
