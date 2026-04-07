import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CellService from '@/entities/cell/cell.service';
import MemberService from '@/entities/member/member.service';
import AlertService from '@/shared/alert/alert.service';

import MemberCelulaUpdate from './member-celula-update.vue';
import MemberCelulaService from './member-celula.service';

type MemberCelulaUpdateComponentType = InstanceType<typeof MemberCelulaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const memberCelulaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MemberCelulaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MemberCelula Management Update Component', () => {
    let comp: MemberCelulaUpdateComponentType;
    let memberCelulaServiceStub: SinonStubbedInstance<MemberCelulaService>;

    beforeEach(() => {
      route = {};
      memberCelulaServiceStub = sinon.createStubInstance<MemberCelulaService>(MemberCelulaService);
      memberCelulaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          memberCelulaService: () => memberCelulaServiceStub,
          memberService: () =>
            sinon.createStubInstance<MemberService>(MemberService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          cellService: () =>
            sinon.createStubInstance<CellService>(CellService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MemberCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.memberCelula = memberCelulaSample;
        memberCelulaServiceStub.update.resolves(memberCelulaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(memberCelulaServiceStub.update.calledWith(memberCelulaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        memberCelulaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MemberCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.memberCelula = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(memberCelulaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        memberCelulaServiceStub.find.resolves(memberCelulaSample);
        memberCelulaServiceStub.retrieve.resolves([memberCelulaSample]);

        // WHEN
        route = {
          params: {
            memberCelulaId: `${memberCelulaSample.id}`,
          },
        };
        const wrapper = shallowMount(MemberCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.memberCelula).toMatchObject(memberCelulaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        memberCelulaServiceStub.find.resolves(memberCelulaSample);
        const wrapper = shallowMount(MemberCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
