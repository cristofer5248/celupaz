import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import IglesiaService from '@/entities/iglesia/iglesia.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type IIglesia } from '@/shared/model/iglesia.model';
import { type IMember, Member } from '@/shared/model/member.model';

import MemberService from './member.service';

export default defineComponent({
  name: 'MemberUpdate',
  setup() {
    const memberService = inject('memberService', () => new MemberService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const member: Ref<IMember> = ref(new Member());

    const iglesiaService = inject('iglesiaService', () => new IglesiaService());

    const iglesias: Ref<IIglesia[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      iglesiaService()
        .retrieve()
        .then(res => {
          iglesias.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {},
      phone: {},
      department: {},
      municipality: {},
      colony: {},
      isCompaz: {},
      fechacumple: {},
      padre: {},
      relacion: {},
      iglesia: {},
      createdBy: {},
    };
    const v$ = useVuelidate(validationRules, member as any);
    v$.value.$validate();

    return {
      memberService,
      alertService,
      member,
      previousState,
      isSaving,
      currentLanguage,
      iglesias,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.member.id) {
        this.memberService()
          .update(this.member)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.member.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.memberService()
          .create(this.member)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.member.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
