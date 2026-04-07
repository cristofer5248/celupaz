import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IPrivilege, Privilege } from '@/shared/model/privilege.model';

import PrivilegeService from './privilege.service';

export default defineComponent({
  name: 'PrivilegeUpdate',
  setup() {
    const privilegeService = inject('privilegeService', () => new PrivilegeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const privilege: Ref<IPrivilege> = ref(new Privilege());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePrivilege = async privilegeId => {
      try {
        const res = await privilegeService().find(privilegeId);
        privilege.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.privilegeId) {
      retrievePrivilege(route.params.privilegeId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, privilege as any);
    v$.value.$validate();

    return {
      privilegeService,
      alertService,
      privilege,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.privilege.id) {
        this.privilegeService()
          .update(this.privilege)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.privilege.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.privilegeService()
          .create(this.privilege)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.privilege.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
