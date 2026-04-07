import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAttendance } from '@/shared/model/attendance.model';

import AttendanceService from './attendance.service';

export default defineComponent({
  name: 'AttendanceDetails',
  setup() {
    const attendanceService = inject('attendanceService', () => new AttendanceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const attendance: Ref<IAttendance> = ref({});

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

    return {
      alertService,
      attendance,

      previousState,
      t$: useI18n().t,
    };
  },
});
