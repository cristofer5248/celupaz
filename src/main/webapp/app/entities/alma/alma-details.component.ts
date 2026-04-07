import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IAlma } from '@/shared/model/alma.model';

import AlmaService from './alma.service';

export default defineComponent({
  name: 'AlmaDetails',
  setup() {
    const almaService = inject('almaService', () => new AlmaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const alma: Ref<IAlma> = ref({});

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

    return {
      alertService,
      alma,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
