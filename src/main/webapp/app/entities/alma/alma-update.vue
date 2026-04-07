<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="celupazmasterApp.alma.home.createOrEditLabel" data-cy="AlmaCreateUpdateHeading">
          {{ t$('celupazmasterApp.alma.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="alma.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="alma.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.name') }}</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="alma-name"
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
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.email') }}</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="alma-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.phone') }}</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="alma-phone"
              data-cy="phone"
              :class="{ valid: !v$.phone.$invalid, invalid: v$.phone.$invalid }"
              v-model="v$.phone.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.department') }}</label>
            <input
              type="text"
              class="form-control"
              name="department"
              id="alma-department"
              data-cy="department"
              :class="{ valid: !v$.department.$invalid, invalid: v$.department.$invalid }"
              v-model="v$.department.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.municipality') }}</label>
            <input
              type="text"
              class="form-control"
              name="municipality"
              id="alma-municipality"
              data-cy="municipality"
              :class="{ valid: !v$.municipality.$invalid, invalid: v$.municipality.$invalid }"
              v-model="v$.municipality.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.colony') }}</label>
            <input
              type="text"
              class="form-control"
              name="colony"
              id="alma-colony"
              data-cy="colony"
              :class="{ valid: !v$.colony.$invalid, invalid: v$.colony.$invalid }"
              v-model="v$.colony.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.description') }}</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="alma-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="alma">{{ t$('celupazmasterApp.alma.foto') }}</label>
            <div>
              <img :src="'data:' + alma.fotoContentType + ';base64,' + alma.foto" style="max-height: 100px" v-if="alma.foto" alt="alma" />
              <div v-if="alma.foto" class="form-text text-danger clearfix">
                <span class="pull-start">{{ alma.fotoContentType }}, {{ byteSize(alma.foto) }}</span>
                <button
                  type="button"
                  @click="clearInputImage('foto', 'fotoContentType', 'file_foto')"
                  class="btn btn-secondary btn-xs pull-end"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <label for="file_foto" class="btn btn-primary pull-end">{{ t$('entity.action.addimage') }}</label>
              <input
                type="file"
                ref="file_foto"
                id="file_foto"
                style="display: none"
                data-cy="foto"
                @change="setFileData($event, alma, 'foto', true)"
                accept="image/*"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="foto"
              id="alma-foto"
              data-cy="foto"
              :class="{ valid: !v$.foto.$invalid, invalid: v$.foto.$invalid }"
              v-model="v$.foto.$model"
            />
            <input type="hidden" class="form-control" name="fotoContentType" id="alma-fotoContentType" v-model="alma.fotoContentType" />
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
<script lang="ts" src="./alma-update.component.ts"></script>
