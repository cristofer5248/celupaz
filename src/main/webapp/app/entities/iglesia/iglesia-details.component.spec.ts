import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import IglesiaDetails from './iglesia-details.vue';
import IglesiaService from './iglesia.service';

type IglesiaDetailsComponentType = InstanceType<typeof IglesiaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const iglesiaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Iglesia Management Detail Component', () => {
    let iglesiaServiceStub: SinonStubbedInstance<IglesiaService>;
    let mountOptions: MountingOptions<IglesiaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      iglesiaServiceStub = sinon.createStubInstance<IglesiaService>(IglesiaService);

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
          iglesiaService: () => iglesiaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        iglesiaServiceStub.find.resolves(iglesiaSample);
        route = {
          params: {
            iglesiaId: `${123}`,
          },
        };
        const wrapper = shallowMount(IglesiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.iglesia).toMatchObject(iglesiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        iglesiaServiceStub.find.resolves(iglesiaSample);
        const wrapper = shallowMount(IglesiaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
