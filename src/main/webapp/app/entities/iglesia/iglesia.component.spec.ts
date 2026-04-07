import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import IglesiaService from './iglesia.service';
import Iglesia from './iglesia.vue';

type IglesiaComponentType = InstanceType<typeof Iglesia>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Iglesia Management Component', () => {
    let iglesiaServiceStub: SinonStubbedInstance<IglesiaService>;
    let mountOptions: MountingOptions<IglesiaComponentType>['global'];

    beforeEach(() => {
      iglesiaServiceStub = sinon.createStubInstance<IglesiaService>(IglesiaService);
      iglesiaServiceStub.retrieve.resolves({ headers: {} });

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
          iglesiaService: () => iglesiaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        iglesiaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Iglesia, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(iglesiaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.iglesias[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: IglesiaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Iglesia, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        iglesiaServiceStub.retrieve.reset();
        iglesiaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        iglesiaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeIglesia();
        await comp.$nextTick(); // clear components

        // THEN
        expect(iglesiaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(iglesiaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
