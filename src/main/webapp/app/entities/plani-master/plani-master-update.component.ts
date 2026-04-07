import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IPlaniMaster, PlaniMaster } from '@/shared/model/plani-master.model';

import PlaniMasterService from './plani-master.service';

export default defineComponent({
  name: 'PlaniMasterUpdate',
  setup() {
    const planiMasterService = inject('planiMasterService', () => new PlaniMasterService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const planiMaster: Ref<IPlaniMaster> = ref(new PlaniMaster());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePlaniMaster = async planiMasterId => {
      try {
        const res = await planiMasterService().find(planiMasterId);
        planiMaster.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planiMasterId) {
      retrievePlaniMaster(route.params.planiMasterId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fecha: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      ofrenda: {},
      visitaCordinador: {},
      visitaTutor: {},
      visitaDirector: {},
      otraVisita: {},
      note: {},
      doneby: {},
      completado: {},
    };
    const v$ = useVuelidate(validationRules, planiMaster as any);
    v$.value.$validate();

    return {
      planiMasterService,
      alertService,
      planiMaster,
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
      if (this.planiMaster.id) {
        this.planiMasterService()
          .update(this.planiMaster)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.planiMaster.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.planiMasterService()
          .create(this.planiMaster)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.planiMaster.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
