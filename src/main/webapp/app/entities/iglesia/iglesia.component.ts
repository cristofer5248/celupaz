import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IIglesia } from '@/shared/model/iglesia.model';

import IglesiaService from './iglesia.service';

export default defineComponent({
  name: 'Iglesia',
  setup() {
    const { t: t$ } = useI18n();
    const iglesiaService = inject('iglesiaService', () => new IglesiaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const iglesias: Ref<IIglesia[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveIglesias = async () => {
      isFetching.value = true;
      try {
        const res = await iglesiaService().retrieve();
        iglesias.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveIglesias();
    };

    onMounted(async () => {
      await retrieveIglesias();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IIglesia) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeIglesia = async () => {
      try {
        await iglesiaService().delete(removeId.value);
        const message = t$('celupazmasterApp.iglesia.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveIglesias();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      iglesias,
      handleSyncList,
      isFetching,
      retrieveIglesias,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeIglesia,
      t$,
    };
  },
});
