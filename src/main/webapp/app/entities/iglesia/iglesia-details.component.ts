import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IIglesia } from '@/shared/model/iglesia.model';

import IglesiaService from './iglesia.service';

export default defineComponent({
  name: 'IglesiaDetails',
  setup() {
    const iglesiaService = inject('iglesiaService', () => new IglesiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const iglesia: Ref<IIglesia> = ref({});

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

    return {
      alertService,
      iglesia,

      previousState,
      t$: useI18n().t,
    };
  },
});
