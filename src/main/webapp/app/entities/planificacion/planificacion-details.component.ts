import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPlanificacion } from '@/shared/model/planificacion.model';

import PlanificacionService from './planificacion.service';

export default defineComponent({
  name: 'PlanificacionDetails',
  setup() {
    const planificacionService = inject('planificacionService', () => new PlanificacionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const planificacion: Ref<IPlanificacion> = ref({});

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

    return {
      alertService,
      planificacion,

      previousState,
      t$: useI18n().t,
    };
  },
});
