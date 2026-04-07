import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IRolCelula, RolCelula } from '@/shared/model/rol-celula.model';

import RolCelulaService from './rol-celula.service';

export default defineComponent({
  name: 'RolCelulaUpdate',
  setup() {
    const rolCelulaService = inject('rolCelulaService', () => new RolCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const rolCelula: Ref<IRolCelula> = ref(new RolCelula());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveRolCelula = async rolCelulaId => {
      try {
        const res = await rolCelulaService().find(rolCelulaId);
        rolCelula.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.rolCelulaId) {
      retrieveRolCelula(route.params.rolCelulaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, rolCelula as any);
    v$.value.$validate();

    return {
      rolCelulaService,
      alertService,
      rolCelula,
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
      if (this.rolCelula.id) {
        this.rolCelulaService()
          .update(this.rolCelula)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.rolCelula.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.rolCelulaService()
          .create(this.rolCelula)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.rolCelula.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
