import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IPrivilege } from '@/shared/model/privilege.model';

import PrivilegeService from './privilege.service';

export default defineComponent({
  name: 'PrivilegeDetails',
  setup() {
    const privilegeService = inject('privilegeService', () => new PrivilegeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const privilege: Ref<IPrivilege> = ref({});

    const retrievePrivilege = async privilegeId => {
      try {
        const res = await privilegeService().find(privilegeId);
        privilege.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.privilegeId) {
      retrievePrivilege(route.params.privilegeId);
    }

    return {
      alertService,
      privilege,

      previousState,
      t$: useI18n().t,
    };
  },
});
