import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IMemberCelula } from '@/shared/model/member-celula.model';

import MemberCelulaService from './member-celula.service';

export default defineComponent({
  name: 'MemberCelulaDetails',
  setup() {
    const memberCelulaService = inject('memberCelulaService', () => new MemberCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const memberCelula: Ref<IMemberCelula> = ref({});

    const retrieveMemberCelula = async memberCelulaId => {
      try {
        const res = await memberCelulaService().find(memberCelulaId);
        memberCelula.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.memberCelulaId) {
      retrieveMemberCelula(route.params.memberCelulaId);
    }

    return {
      alertService,
      memberCelula,

      previousState,
      t$: useI18n().t,
    };
  },
});
