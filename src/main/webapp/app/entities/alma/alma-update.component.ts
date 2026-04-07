import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { Alma, type IAlma } from '@/shared/model/alma.model';

import AlmaService from './alma.service';

export default defineComponent({
  name: 'AlmaUpdate',
  setup() {
    const almaService = inject('almaService', () => new AlmaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const alma: Ref<IAlma> = ref(new Alma());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAlma = async almaId => {
      try {
        const res = await almaService().find(almaId);
        alma.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.almaId) {
      retrieveAlma(route.params.almaId);
    }

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {},
      phone: {},
      department: {},
      municipality: {},
      colony: {},
      description: {},
      foto: {},
    };
    const v$ = useVuelidate(validationRules, alma as any);
    v$.value.$validate();

    return {
      almaService,
      alertService,
      alma,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.alma.id) {
        this.almaService()
          .update(this.alma)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.alma.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.almaService()
          .create(this.alma)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.alma.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    clearInputImage(field, fieldContentType, idInput): void {
      if (this.alma && field && fieldContentType) {
        if (Object.hasOwn(this.alma, field)) {
          this.alma[field] = null;
        }
        if (Object.hasOwn(this.alma, fieldContentType)) {
          this.alma[fieldContentType] = null;
        }
        if (idInput) {
          (<any>this).$refs[idInput] = null;
        }
      }
    },
  },
});
