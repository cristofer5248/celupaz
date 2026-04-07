import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import RolCelulaDetails from './rol-celula-details.vue';
import RolCelulaService from './rol-celula.service';

type RolCelulaDetailsComponentType = InstanceType<typeof RolCelulaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const rolCelulaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('RolCelula Management Detail Component', () => {
    let rolCelulaServiceStub: SinonStubbedInstance<RolCelulaService>;
    let mountOptions: MountingOptions<RolCelulaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      rolCelulaServiceStub = sinon.createStubInstance<RolCelulaService>(RolCelulaService);

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
          rolCelulaService: () => rolCelulaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        rolCelulaServiceStub.find.resolves(rolCelulaSample);
        route = {
          params: {
            rolCelulaId: `${123}`,
          },
        };
        const wrapper = shallowMount(RolCelulaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.rolCelula).toMatchObject(rolCelulaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        rolCelulaServiceStub.find.resolves(rolCelulaSample);
        const wrapper = shallowMount(RolCelulaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
