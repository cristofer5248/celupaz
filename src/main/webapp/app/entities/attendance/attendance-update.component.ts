import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import MemberCelulaService from '@/entities/member-celula/member-celula.service';
import PlanificacionService from '@/entities/planificacion/planificacion.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { Attendance, type IAttendance } from '@/shared/model/attendance.model';
import { type IMemberCelula } from '@/shared/model/member-celula.model';
import { type IPlanificacion } from '@/shared/model/planificacion.model';

import AttendanceService from './attendance.service';

const getTodayDateString = () => {
  const today = new Date();
  const offset = today.getTimezoneOffset();
  const todayWithOffset = new Date(today.getTime() - offset * 60 * 1000);
  return todayWithOffset.toISOString().split('T')[0];
};

export default defineComponent({
  name: 'AttendanceUpdate',
  setup() {
    const attendanceService = inject('attendanceService', () => new AttendanceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const attendance: Ref<IAttendance> = ref(new Attendance());
    const isEdit = ref(false);

    // Multi-creation state
    const sharedPlanificacion = ref<IPlanificacion | null>(null);
    const sharedFecha = ref<string>(getTodayDateString());
    const selectedMemberCelulas: Ref<IMemberCelula[]> = ref([]); // Use an array for checkbox v-model

    const memberCelulaService = inject('memberCelulaService', () => new MemberCelulaService());
    const memberCelulas: Ref<IMemberCelula[]> = ref([]);

    const planificacionService = inject('planificacionService', () => new PlanificacionService());
    const planificacions: Ref<IPlanificacion[]> = ref([]);

    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAttendance = async attendanceId => {
      try {
        const res = await attendanceService().find(attendanceId);
        attendance.value = res;
        isEdit.value = true;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.attendanceId) {
      retrieveAttendance(route.params.attendanceId);
    }

    const initRelationships = () => {
      memberCelulaService()
        .retrieve()
        .then(res => {
          memberCelulas.value = res.data;
        });
      planificacionService()
        .retrieve()
        .then(res => {
          planificacions.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      membercelula: {},
      planificacion: {},
    };
    const v$ = useVuelidate(validationRules, attendance as any);
    v$.value.$validate();

    const isFormValid = computed(() => {
      if (isEdit.value) {
        // Validation for edit mode remains the same
        return attendance.value.membercelula != null && attendance.value.planificacion != null;
      } else {
        // Validation for create mode: planificacion must be selected and at least one member must be checked.
        const isSharedValid = sharedPlanificacion.value !== null;
        const areItemsValid = selectedMemberCelulas.value.length > 0;
        return isSharedValid && areItemsValid;
      }
    });

    return {
      attendanceService,
      alertService,
      attendance,
      isEdit,
      sharedPlanificacion,
      sharedFecha,
      selectedMemberCelulas, // Expose to template
      isFormValid,
      previousState,
      isSaving,
      currentLanguage,
      memberCelulas,
      planificacions,
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
          this.attendance.fecha = currentDate as any;
          const param = await this.attendanceService().update(this.attendance);
          this.isSaving = false;
          this.previousState();
          this.alertService.showInfo(this.t$('celupazmasterApp.attendance.updated', { param: param.id }));
        } catch (error: any) {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        }
      } else {
        try {
          // Iterate over the array of selected members
          const promises = this.selectedMemberCelulas.map(member => {
            const newAttendance = new Attendance();
            newAttendance.fecha = currentDate as any;
            newAttendance.planificacion = this.sharedPlanificacion as any;
            newAttendance.membercelula = member; // Assign the selected member
            return this.attendanceService().create(newAttendance);
          });

          await Promise.all(promises);

          this.isSaving = false;
          this.previousState();
          this.alertService.showSuccess(this.t$('celupazmasterApp.attendance.created', { param: 'Múltiples' }).toString());
        } catch (error: any) {
          this.isSaving = false;
          this.alertService.showHttpError(error.response);
        }
      }
    },
  },
});
