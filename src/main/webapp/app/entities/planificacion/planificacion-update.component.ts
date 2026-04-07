import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AlmaHistoryService from '@/entities/alma-history/alma-history.service';
import PlaniMasterService from '@/entities/plani-master/plani-master.service';
import PrivilegeService from '@/entities/privilege/privilege.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IAlmaHistory } from '@/shared/model/alma-history.model';
import { type IPlaniMaster } from '@/shared/model/plani-master.model';
import { type IPlanificacion, Planificacion } from '@/shared/model/planificacion.model';
import { type IPrivilege } from '@/shared/model/privilege.model';

import PlanificacionService from './planificacion.service';

export default defineComponent({
  name: 'PlanificacionUpdate',
  setup() {
    const planificacionService = inject('planificacionService', () => new PlanificacionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const planificacion: Ref<IPlanificacion> = ref(new Planificacion());

    const almaHistoryService = inject('almaHistoryService', () => new AlmaHistoryService());

    const almaHistories: Ref<IAlmaHistory[]> = ref([]);

    const privilegeService = inject('privilegeService', () => new PrivilegeService());

    const privileges: Ref<IPrivilege[]> = ref([]);

    const planiMasterService = inject('planiMasterService', () => new PlaniMasterService());

    const planiMasters: Ref<IPlaniMaster[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePlanificacion = async planificacionId => {
      try {
        const res = await planificacionService().find(planificacionId);
        planificacion.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planificacionId) {
      retrievePlanificacion(route.params.planificacionId);
    }

    const initRelationships = () => {
      almaHistoryService()
        .retrieve()
        .then(res => {
          almaHistories.value = res.data;
        });
      privilegeService()
        .retrieve()
        .then(res => {
          privileges.value = res.data;
        });
      planiMasterService()
        .retrieve()
        .then(res => {
          planiMasters.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fecha: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      almahistory: {},
      privilege: {},
      planiMaster: {},
    };
    const v$ = useVuelidate(validationRules, planificacion as any);
    v$.value.$validate();

    return {
      planificacionService,
      alertService,
      planificacion,
      previousState,
      isSaving,
      currentLanguage,
      almaHistories,
      privileges,
      planiMasters,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.planificacion.id) {
        this.planificacionService()
          .update(this.planificacion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.planificacion.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.planificacionService()
          .create(this.planificacion)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.planificacion.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
