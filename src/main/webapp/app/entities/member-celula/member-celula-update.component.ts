import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import CellService from '@/entities/cell/cell.service';
import MemberService from '@/entities/member/member.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ICell } from '@/shared/model/cell.model';
import { type IMemberCelula, MemberCelula } from '@/shared/model/member-celula.model';
import { type IMember } from '@/shared/model/member.model';

import MemberCelulaService from './member-celula.service';

export default defineComponent({
  name: 'MemberCelulaUpdate',
  setup() {
    const memberCelulaService = inject('memberCelulaService', () => new MemberCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const memberCelula: Ref<IMemberCelula> = ref(new MemberCelula());

    const memberService = inject('memberService', () => new MemberService());

    const members: Ref<IMember[]> = ref([]);

    const cellService = inject('cellService', () => new CellService());

    const cells: Ref<ICell[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      memberService()
        .retrieve()
        .then(res => {
          members.value = res.data;
        });
      cellService()
        .retrieve()
        .then(res => {
          cells.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaCreada: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      enabled: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      member: {},
      cell: {},
    };
    const v$ = useVuelidate(validationRules, memberCelula as any);
    v$.value.$validate();

    return {
      memberCelulaService,
      alertService,
      memberCelula,
      previousState,
      isSaving,
      currentLanguage,
      members,
      cells,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.memberCelula.id) {
        this.memberCelulaService()
          .update(this.memberCelula)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.memberCelula.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.memberCelulaService()
          .create(this.memberCelula)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.memberCelula.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
