import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import MemberCelulaService from './member-celula.service';
import MemberCelula from './member-celula.vue';

type MemberCelulaComponentType = InstanceType<typeof MemberCelula>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MemberCelula Management Component', () => {
    let memberCelulaServiceStub: SinonStubbedInstance<MemberCelulaService>;
    let mountOptions: MountingOptions<MemberCelulaComponentType>['global'];

    beforeEach(() => {
      memberCelulaServiceStub = sinon.createStubInstance<MemberCelulaService>(MemberCelulaService);
      memberCelulaServiceStub.retrieve.resolves({ headers: {} });

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
          memberCelulaService: () => memberCelulaServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        memberCelulaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MemberCelula, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(memberCelulaServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.memberCelulas[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MemberCelulaComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MemberCelula, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        memberCelulaServiceStub.retrieve.reset();
        memberCelulaServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        memberCelulaServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMemberCelula();
        await comp.$nextTick(); // clear components

        // THEN
        expect(memberCelulaServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(memberCelulaServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
