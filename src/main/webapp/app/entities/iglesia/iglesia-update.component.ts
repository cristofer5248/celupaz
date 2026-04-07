import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IIglesia, Iglesia } from '@/shared/model/iglesia.model';

import IglesiaService from './iglesia.service';

export default defineComponent({
  name: 'IglesiaUpdate',
  setup() {
    const iglesiaService = inject('iglesiaService', () => new IglesiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const iglesia: Ref<IIglesia> = ref(new Iglesia());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveIglesia = async iglesiaId => {
      try {
        const res = await iglesiaService().find(iglesiaId);
        iglesia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.iglesiaId) {
      retrieveIglesia(route.params.iglesiaId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, iglesia as any);
    v$.value.$validate();

    return {
      iglesiaService,
      alertService,
      iglesia,
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
      if (this.iglesia.id) {
        this.iglesiaService()
          .update(this.iglesia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.iglesia.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.iglesiaService()
          .create(this.iglesia)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.iglesia.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
