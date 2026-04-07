import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IAlmaHistory } from '@/shared/model/alma-history.model';

import AlmaHistoryService from './alma-history.service';

export default defineComponent({
  name: 'AlmaHistoryDetails',
  setup() {
    const almaHistoryService = inject('almaHistoryService', () => new AlmaHistoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const almaHistory: Ref<IAlmaHistory> = ref({});

    const retrieveAlmaHistory = async almaHistoryId => {
      try {
        const res = await almaHistoryService().find(almaHistoryId);
        almaHistory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.almaHistoryId) {
      retrieveAlmaHistory(route.params.almaHistoryId);
    }

    return {
      alertService,
      almaHistory,

      previousState,
      t$: useI18n().t,
    };
  },
});
