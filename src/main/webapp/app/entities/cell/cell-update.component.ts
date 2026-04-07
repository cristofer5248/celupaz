import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useVuelidate } from '@vuelidate/core';

import CellTypeService from '@/entities/cell-type/cell-type.service';
import { useAlertService } from '@/shared/alert/alert.service';
import { useValidation } from '@/shared/composables';
import { type ICellType } from '@/shared/model/cell-type.model';
import { Cell, type ICell } from '@/shared/model/cell.model';

import CellService from './cell.service';

export default defineComponent({
  name: 'CellUpdate',
  setup() {
    const cellService = inject('cellService', () => new CellService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cell: Ref<ICell> = ref(new Cell());

    const cellTypeService = inject('cellTypeService', () => new CellTypeService());

    const cellTypes: Ref<ICellType[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCell = async cellId => {
      try {
        const res = await cellService().find(cellId);
        cell.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cellId) {
      retrieveCell(route.params.cellId);
    }

    const initRelationships = () => {
      cellTypeService()
        .retrieve()
        .then(res => {
          cellTypes.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {},
      startDate: {},
      description: {},
      sector: {},
      lider: {},
      cordinador: {},
      cellType: {},
    };
    const v$ = useVuelidate(validationRules, cell as any);
    v$.value.$validate();

    return {
      cellService,
      alertService,
      cell,
      previousState,
      isSaving,
      currentLanguage,
      cellTypes,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cell.id) {
        this.cellService()
          .update(this.cell)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('celupazmasterApp.cell.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cellService()
          .create(this.cell)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('celupazmasterApp.cell.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
