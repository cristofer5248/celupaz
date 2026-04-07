<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.memberCelula.home.createOrEditLabel" data-cy="MemberCelulaCreateUpdateHeading">
          {{ t$('celupazmasterApp.memberCelula.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="memberCelula.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="memberCelula.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member-celula">{{ t$('celupazmasterApp.memberCelula.fechaCreada') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="member-celula-fechaCreada"
                  v-model="v$.fechaCreada.$model"
                  name="fechaCreada"
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
                id="member-celula-fechaCreada"
                data-cy="fechaCreada"
                type="text"
                class="form-control"
                name="fechaCreada"
                :class="{ valid: !v$.fechaCreada.$invalid, invalid: v$.fechaCreada.$invalid }"
                v-model="v$.fechaCreada.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaCreada.$anyDirty && v$.fechaCreada.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaCreada.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member-celula">{{ t$('celupazmasterApp.memberCelula.enabled') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="enabled"
              id="member-celula-enabled"
              data-cy="enabled"
              :class="{ valid: !v$.enabled.$invalid, invalid: v$.enabled.$invalid }"
              v-model="v$.enabled.$model"
              required
            />
            <div v-if="v$.enabled.$anyDirty && v$.enabled.$invalid">
              <small class="form-text text-danger" v-for="error of v$.enabled.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member-celula">{{ t$('celupazmasterApp.memberCelula.member') }}</label>
            <select class="form-control" id="member-celula-member" data-cy="member" name="member" v-model="memberCelula.member">
              <option :value="null"></option>
              <option
                :value="memberCelula.member && memberOption.id === memberCelula.member.id ? memberCelula.member : memberOption"
                v-for="memberOption in members"
                :key="memberOption.id"
              >
                {{ memberOption.name }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member-celula">{{ t$('celupazmasterApp.memberCelula.cell') }}</label>
            <select class="form-control" id="member-celula-cell" data-cy="cell" name="cell" v-model="memberCelula.cell">
              <option :value="null"></option>
              <option
                :value="memberCelula.cell && cellOption.id === memberCelula.cell.id ? memberCelula.cell : cellOption"
                v-for="cellOption in cells"
                :key="cellOption.id"
              >
                {{ cellOption.name }}
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
<script lang="ts" src="./member-celula-update.component.ts"></script>
