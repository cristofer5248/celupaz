import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ICell } from '@/shared/model/cell.model';

import CellService from './cell.service';

export default defineComponent({
  name: 'CellDetails',
  setup() {
    const cellService = inject('cellService', () => new CellService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cell: Ref<ICell> = ref({});

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

    return {
      alertService,
      cell,

      previousState,
      t$: useI18n().t,
    };
  },
});
