import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PrivilegeDetails from './privilege-details.vue';
import PrivilegeService from './privilege.service';

type PrivilegeDetailsComponentType = InstanceType<typeof PrivilegeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const privilegeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Privilege Management Detail Component', () => {
    let privilegeServiceStub: SinonStubbedInstance<PrivilegeService>;
    let mountOptions: MountingOptions<PrivilegeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      privilegeServiceStub = sinon.createStubInstance<PrivilegeService>(PrivilegeService);

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
          privilegeService: () => privilegeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        privilegeServiceStub.find.resolves(privilegeSample);
        route = {
          params: {
            privilegeId: `${123}`,
          },
        };
        const wrapper = shallowMount(PrivilegeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.privilege).toMatchObject(privilegeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        privilegeServiceStub.find.resolves(privilegeSample);
        const wrapper = shallowMount(PrivilegeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
