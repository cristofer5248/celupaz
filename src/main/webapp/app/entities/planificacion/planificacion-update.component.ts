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

// Helper to get today's date as YYYY-MM-DD string, adjusted for local timezone
const getTodayDateString = () => {
  const today = new Date();
  const offset = today.getTimezoneOffset();
  const todayWithOffset = new Date(today.getTime() - offset * 60 * 1000);
  return todayWithOffset.toISOString().split('T')[0];
};

export default defineComponent({
  name: 'PlanificacionUpdate',
  setup() {
    const planificacionService = inject('planificacionService', () => new PlanificacionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const planificacion: Ref<IPlanificacion> = ref(new Planificacion());
    const isEdit = ref(false);

    // Multi-creation state
    const sharedPlaniMaster = ref<IPlaniMaster | null>(null);
    const sharedFecha = ref<string>(getTodayDateString()); // Always use today's date
    const items = ref<{ almahistory: IAlmaHistory | null; privilege: IPrivilege | null }[]>([{ almahistory: null, privilege: null }]);

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
        isEdit.value = true;
      } catch (error: any) {
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
    // Remove fecha validation
    const validationRules = {
      almahistory: {},
      privilege: {},
      planiMaster: {},
    };
    const v$ = useVuelidate(validationRules, planificacion as any);
    v$.value.$validate();

    const addItem = () => {
      items.value.push({ almahistory: null, privilege: null });
    };

    const removeItem = (index: number) => {
      if (items.value.length > 1) {
        items.value.splice(index, 1);
      }
    };

    const getAvailableAlmaHistories = (currentIndex: number) => {
      const selectedIds = items.value
        .map((item, index) => (index !== currentIndex && item.almahistory ? item.almahistory.id : null))
        .filter(id => id !== null);
      return almaHistories.value.filter(ah => ah.id && !selectedIds.includes(ah.id));
    };

    const isFormValid = computed(() => {
      if (isEdit.value) {
        return !v$.value.$invalid;
      } else {
        // sharedFecha is always valid now.
        const isSharedValid = sharedPlaniMaster.value !== null;
        const areItemsValid = items.value.length > 0 && items.value.every(item => item.almahistory !== null && item.privilege !== null);

        const selectedIds = items.value.map(item => item.almahistory?.id).filter(id => id != null);
        const hasDuplicates = new Set(selectedIds).size !== selectedIds.length;

        return isSharedValid && areItemsValid && !hasDuplicates;
      }
    });

    return {
      planificacionService,
      alertService,
      planificacion,
      isEdit,
      sharedPlaniMaster,
      sharedFecha, // Pass to template
      items,
      addItem,
      removeItem,
      getAvailableAlmaHistories,
      isFormValid,
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
    async save(): Promise<void> {
      this.isSaving = true;
      const currentDate = getTodayDateString();

      if (this.isEdit) {
        try {
          // Set the date to current date before updating
          this.planificacion.fecha = currentDate as any;
          const param = await this.planificacionService().update(this.planificacion);
          this.isSaving = false;
          this.previousState();
          this.alertService.showInfo(this.t$('celupazmasterApp.planificacion.updated', { param: param.id }));
        } catch (error: any) {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        }
      } else {
        try {
          const promises = this.items.map(item => {
            const newPlanificacion = new Planificacion();
            newPlanificacion.fecha = currentDate as any; // Use current date
            newPlanificacion.planiMaster = this.sharedPlaniMaster as any;
            newPlanificacion.almahistory = item.almahistory as any;
            newPlanificacion.privilege = item.privilege as any;
            return this.planificacionService().create(newPlanificacion);
          });

          await Promise.all(promises);

          this.isSaving = false;
          this.previousState();
          this.alertService.showSuccess(this.t$('celupazmasterApp.planificacion.created', { param: 'Múltiples' }).toString());
        } catch (error: any) {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        }
      }
    },
  },
});
