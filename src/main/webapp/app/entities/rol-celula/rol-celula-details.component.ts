import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IRolCelula } from '@/shared/model/rol-celula.model';

import RolCelulaService from './rol-celula.service';

export default defineComponent({
  name: 'RolCelulaDetails',
  setup() {
    const rolCelulaService = inject('rolCelulaService', () => new RolCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const rolCelula: Ref<IRolCelula> = ref({});

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

    return {
      alertService,
      rolCelula,

      previousState,
      t$: useI18n().t,
    };
  },
});
