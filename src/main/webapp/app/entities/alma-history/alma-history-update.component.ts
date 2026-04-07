import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import AlmaService from '@/entities/alma/alma.service';
import CellService from '@/entities/cell/cell.service';
import RolCelulaService from '@/entities/rol-celula/rol-celula.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { AlmaHistory, type IAlmaHistory } from '@/shared/model/alma-history.model';
import { type IAlma } from '@/shared/model/alma.model';
import { type ICell } from '@/shared/model/cell.model';
import { type IRolCelula } from '@/shared/model/rol-celula.model';

import AlmaHistoryService from './alma-history.service';

export default defineComponent({
  name: 'AlmaHistoryUpdate',
  setup() {
    const almaHistoryService = inject('almaHistoryService', () => new AlmaHistoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const almaHistory: Ref<IAlmaHistory> = ref(new AlmaHistory());

    const almaService = inject('almaService', () => new AlmaService());

    const almas: Ref<IAlma[]> = ref([]);

    const cellService = inject('cellService', () => new CellService());

    const cells: Ref<ICell[]> = ref([]);

    const rolCelulaService = inject('rolCelulaService', () => new RolCelulaService());

    const rolCelulas: Ref<IRolCelula[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      almaService()
        .retrieve()
        .then(res => {
          almas.value = res.data;
        });
      cellService()
        .retrieve()
        .then(res => {
          cells.value = res.data;
        });
      rolCelulaService()
        .retrieve()
        .then(res => {
          rolCelulas.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fecha: {},
      alma: {},
      cell: {},
      rolcelula: {},
    };
    const v$ = useVuelidate(validationRules, almaHistory as any);
    v$.value.$validate();

    return {
      almaHistoryService,
      alertService,
      almaHistory,
      previousState,
      isSaving,
      currentLanguage,
      almas,
      cells,
      rolCelulas,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.almaHistory.id) {
        this.almaHistoryService()
          .update(this.almaHistory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.almaHistory.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.almaHistoryService()
          .create(this.almaHistory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.almaHistory.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
