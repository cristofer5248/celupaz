import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import RolCelulaService from './rol-celula.service';
import RolCelula from './rol-celula.vue';

type RolCelulaComponentType = InstanceType<typeof RolCelula>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('RolCelula Management Component', () => {
    let rolCelulaServiceStub: SinonStubbedInstance<RolCelulaService>;
    let mountOptions: MountingOptions<RolCelulaComponentType>['global'];

    beforeEach(() => {
      rolCelulaServiceStub = sinon.createStubInstance<RolCelulaService>(RolCelulaService);
      rolCelulaServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          rolCelulaService: () => rolCelulaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        rolCelulaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(RolCelula, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(rolCelulaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.rolCelulas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: RolCelulaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(RolCelula, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        rolCelulaServiceStub.retrieve.reset();
        rolCelulaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        rolCelulaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeRolCelula();
        await comp.$nextTick(); // clear components

        // THEN
        expect(rolCelulaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(rolCelulaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
