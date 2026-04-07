import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IMemberCelula } from '@/shared/model/member-celula.model';

import MemberCelulaService from './member-celula.service';

export default defineComponent({
  name: 'MemberCelula',
  setup() {
    const { t: t$ } = useI18n();
    const memberCelulaService = inject('memberCelulaService', () => new MemberCelulaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const memberCelulas: Ref<IMemberCelula[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMemberCelulas = async () => {
      isFetching.value = true;
      try {
        const res = await memberCelulaService().retrieve();
        memberCelulas.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMemberCelulas();
    };

    onMounted(async () => {
      await retrieveMemberCelulas();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMemberCelula) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMemberCelula = async () => {
      try {
        await memberCelulaService().delete(removeId.value);
        const message = t$('celupazmasterApp.memberCelula.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMemberCelulas();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      memberCelulas,
      handleSyncList,
      isFetching,
      retrieveMemberCelulas,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMemberCelula,
      t$,
    };
  },
});
