<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.member.home.createOrEditLabel" data-cy="MemberCreateUpdateHeading">
          {{ t$('celupazmasterApp.member.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="member.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="member.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.name') }}</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="member-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.email') }}</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="member-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.phone') }}</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="member-phone"
              data-cy="phone"
              :class="{ valid: !v$.phone.$invalid, invalid: v$.phone.$invalid }"
              v-model="v$.phone.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.department') }}</label>
            <input
              type="text"
              class="form-control"
              name="department"
              id="member-department"
              data-cy="department"
              :class="{ valid: !v$.department.$invalid, invalid: v$.department.$invalid }"
              v-model="v$.department.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.municipality') }}</label>
            <input
              type="text"
              class="form-control"
              name="municipality"
              id="member-municipality"
              data-cy="municipality"
              :class="{ valid: !v$.municipality.$invalid, invalid: v$.municipality.$invalid }"
              v-model="v$.municipality.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.colony') }}</label>
            <input
              type="text"
              class="form-control"
              name="colony"
              id="member-colony"
              data-cy="colony"
              :class="{ valid: !v$.colony.$invalid, invalid: v$.colony.$invalid }"
              v-model="v$.colony.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.isCompaz') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="isCompaz"
              id="member-isCompaz"
              data-cy="isCompaz"
              :class="{ valid: !v$.isCompaz.$invalid, invalid: v$.isCompaz.$invalid }"
              v-model="v$.isCompaz.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.fechacumple') }}</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="member-fechacumple"
                  v-model="v$.fechacumple.$model"
                  name="fechacumple"
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
                id="member-fechacumple"
                data-cy="fechacumple"
                type="text"
                class="form-control"
                name="fechacumple"
                :class="{ valid: !v$.fechacumple.$invalid, invalid: v$.fechacumple.$invalid }"
                v-model="v$.fechacumple.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.padre') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="padre"
              id="member-padre"
              data-cy="padre"
              :class="{ valid: !v$.padre.$invalid, invalid: v$.padre.$invalid }"
              v-model="v$.padre.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.relacion') }}</label>
            <input
              type="text"
              class="form-control"
              name="relacion"
              id="member-relacion"
              data-cy="relacion"
              :class="{ valid: !v$.relacion.$invalid, invalid: v$.relacion.$invalid }"
              v-model="v$.relacion.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member-createdBy">Created By</label>
            <span class="form-control-static" style="display: block; padding: 5px">{{ member.createdBy }}</span>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="member">{{ t$('celupazmasterApp.member.iglesia') }}</label>
            <select class="form-control" id="member-iglesia" data-cy="iglesia" name="iglesia" v-model="member.iglesia">
              <option :value="null"></option>
              <option
                :value="member.iglesia && iglesiaOption.id === member.iglesia.id ? member.iglesia : iglesiaOption"
                v-for="iglesiaOption in iglesias"
                :key="iglesiaOption.id"
              >
                {{ iglesiaOption.name }}
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
<script lang="ts" src="./member-update.component.ts"></script>
