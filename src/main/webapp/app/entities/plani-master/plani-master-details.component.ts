import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPlaniMaster } from '@/shared/model/plani-master.model';

import PlaniMasterService from './plani-master.service';

export default defineComponent({
  name: 'PlaniMasterDetails',
  setup() {
    const planiMasterService = inject('planiMasterService', () => new PlaniMasterService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const planiMaster: Ref<IPlaniMaster> = ref({});

    const retrievePlaniMaster = async planiMasterId => {
      try {
        const res = await planiMasterService().find(planiMasterId);
        planiMaster.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planiMasterId) {
      retrievePlaniMaster(route.params.planiMasterId);
    }

    return {
      alertService,
      planiMaster,

      previousState,
      t$: useI18n().t,
    };
  },
});
