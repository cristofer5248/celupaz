import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PlaniMasterDetails from './plani-master-details.vue';
import PlaniMasterService from './plani-master.service';

type PlaniMasterDetailsComponentType = InstanceType<typeof PlaniMasterDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planiMasterSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PlaniMaster Management Detail Component', () => {
    let planiMasterServiceStub: SinonStubbedInstance<PlaniMasterService>;
    let mountOptions: MountingOptions<PlaniMasterDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      planiMasterServiceStub = sinon.createStubInstance<PlaniMasterService>(PlaniMasterService);

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
          planiMasterService: () => planiMasterServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        planiMasterServiceStub.find.resolves(planiMasterSample);
        route = {
          params: {
            planiMasterId: `${123}`,
          },
        };
        const wrapper = shallowMount(PlaniMasterDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.planiMaster).toMatchObject(planiMasterSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planiMasterServiceStub.find.resolves(planiMasterSample);
        const wrapper = shallowMount(PlaniMasterDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
