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

export default defineComponent({
  name: 'AttendanceUpdate',
  setup() {
    const attendanceService = inject('attendanceService', () => new AttendanceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const attendance: Ref<IAttendance> = ref(new Attendance());

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
      } catch (error) {
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
      fecha: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      membercelula: {},
      planificacion: {},
    };
    const v$ = useVuelidate(validationRules, attendance as any);
    v$.value.$validate();

    return {
      attendanceService,
      alertService,
      attendance,
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
    save(): void {
      this.isSaving = true;
      if (this.attendance.id) {
        this.attendanceService()
          .update(this.attendance)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.attendance.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.attendanceService()
          .create(this.attendance)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.attendance.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
