import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICellType } from '@/shared/model/cell-type.model';

import CellTypeService from './cell-type.service';

export default defineComponent({
  name: 'CellTypeDetails',
  setup() {
    const cellTypeService = inject('cellTypeService', () => new CellTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cellType: Ref<ICellType> = ref({});

    const retrieveCellType = async cellTypeId => {
      try {
        const res = await cellTypeService().find(cellTypeId);
        cellType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cellTypeId) {
      retrieveCellType(route.params.cellTypeId);
    }

    return {
      alertService,
      cellType,

      previousState,
      t$: useI18n().t,
    };
  },
});
