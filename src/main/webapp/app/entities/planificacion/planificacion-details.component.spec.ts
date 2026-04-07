import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PlanificacionDetails from './planificacion-details.vue';
import PlanificacionService from './planificacion.service';

type PlanificacionDetailsComponentType = InstanceType<typeof PlanificacionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planificacionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Planificacion Management Detail Component', () => {
    let planificacionServiceStub: SinonStubbedInstance<PlanificacionService>;
    let mountOptions: MountingOptions<PlanificacionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      planificacionServiceStub = sinon.createStubInstance<PlanificacionService>(PlanificacionService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          planificacionService: () => planificacionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        planificacionServiceStub.find.resolves(planificacionSample);
        route = {
          params: {
            planificacionId: `${123}`,
          },
        };
        const wrapper = shallowMount(PlanificacionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.planificacion).toMatchObject(planificacionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planificacionServiceStub.find.resolves(planificacionSample);
        const wrapper = shallowMount(PlanificacionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
