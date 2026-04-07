import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IMember } from '@/shared/model/member.model';

import MemberService from './member.service';

export default defineComponent({
  name: 'MemberDetails',
  setup() {
    const memberService = inject('memberService', () => new MemberService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const member: Ref<IMember> = ref({});

    const retrieveMember = async memberId => {
      try {
        const res = await memberService().find(memberId);
        member.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.memberId) {
      retrieveMember(route.params.memberId);
    }

    return {
      alertService,
      member,

      previousState,
      t$: useI18n().t,
    };
  },
});
