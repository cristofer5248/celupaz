import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlmaHistoryService from '@/entities/alma-history/alma-history.service';
import PlaniMasterService from '@/entities/plani-master/plani-master.service';
import PrivilegeService from '@/entities/privilege/privilege.service';
import AlertService from '@/shared/alert/alert.service';

import PlanificacionUpdate from './planificacion-update.vue';
import PlanificacionService from './planificacion.service';

type PlanificacionUpdateComponentType = InstanceType<typeof PlanificacionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planificacionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PlanificacionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Planificacion Management Update Component', () => {
    let comp: PlanificacionUpdateComponentType;
    let planificacionServiceStub: SinonStubbedInstance<PlanificacionService>;

    beforeEach(() => {
      route = {};
      planificacionServiceStub = sinon.createStubInstance<PlanificacionService>(PlanificacionService);
      planificacionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          planificacionService: () => planificacionServiceStub,
          almaHistoryService: () =>
            sinon.createStubInstance<AlmaHistoryService>(AlmaHistoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          privilegeService: () =>
            sinon.createStubInstance<PrivilegeService>(PrivilegeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          planiMasterService: () =>
            sinon.createStubInstance<PlaniMasterService>(PlaniMasterService, {
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
        const wrapper = shallowMount(PlanificacionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planificacion = planificacionSample;
        planificacionServiceStub.update.resolves(planificacionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planificacionServiceStub.update.calledWith(planificacionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        planificacionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PlanificacionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planificacion = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planificacionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        planificacionServiceStub.find.resolves(planificacionSample);
        planificacionServiceStub.retrieve.resolves([planificacionSample]);

        // WHEN
        route = {
          params: {
            planificacionId: `${planificacionSample.id}`,
          },
        };
        const wrapper = shallowMount(PlanificacionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.planificacion).toMatchObject(planificacionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planificacionServiceStub.find.resolves(planificacionSample);
        const wrapper = shallowMount(PlanificacionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
