import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AlmaDetails from './alma-details.vue';
import AlmaService from './alma.service';

type AlmaDetailsComponentType = InstanceType<typeof AlmaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const almaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Alma Management Detail Component', () => {
    let almaServiceStub: SinonStubbedInstance<AlmaService>;
    let mountOptions: MountingOptions<AlmaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      almaServiceStub = sinon.createStubInstance<AlmaService>(AlmaService);

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
          almaService: () => almaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        almaServiceStub.find.resolves(almaSample);
        route = {
          params: {
            almaId: `${123}`,
          },
        };
        const wrapper = shallowMount(AlmaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.alma).toMatchObject(almaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        almaServiceStub.find.resolves(almaSample);
        const wrapper = shallowMount(AlmaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
