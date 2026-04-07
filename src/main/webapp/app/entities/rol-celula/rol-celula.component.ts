import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IRolCelula } from '@/shared/model/rol-celula.model';

import RolCelulaService from './rol-celula.service';

export default defineComponent({
  name: 'RolCelula',
  setup() {
    const { t: t$ } = useI18n();
    const rolCelulaService = inject('rolCelulaService', () => new RolCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const rolCelulas: Ref<IRolCelula[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveRolCelulas = async () => {
      isFetching.value = true;
      try {
        const res = await rolCelulaService().retrieve();
        rolCelulas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveRolCelulas();
    };

    onMounted(async () => {
      await retrieveRolCelulas();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IRolCelula) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeRolCelula = async () => {
      try {
        await rolCelulaService().delete(removeId.value);
        const message = t$('celupazmasterApp.rolCelula.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveRolCelulas();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      rolCelulas,
      handleSyncList,
      isFetching,
      retrieveRolCelulas,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeRolCelula,
      t$,
    };
  },
});
