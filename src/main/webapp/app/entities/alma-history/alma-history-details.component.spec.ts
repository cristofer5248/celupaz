import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AlmaHistoryDetails from './alma-history-details.vue';
import AlmaHistoryService from './alma-history.service';

type AlmaHistoryDetailsComponentType = InstanceType<typeof AlmaHistoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const almaHistorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AlmaHistory Management Detail Component', () => {
    let almaHistoryServiceStub: SinonStubbedInstance<AlmaHistoryService>;
    let mountOptions: MountingOptions<AlmaHistoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      almaHistoryServiceStub = sinon.createStubInstance<AlmaHistoryService>(AlmaHistoryService);

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
          almaHistoryService: () => almaHistoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        almaHistoryServiceStub.find.resolves(almaHistorySample);
        route = {
          params: {
            almaHistoryId: `${123}`,
          },
        };
        const wrapper = shallowMount(AlmaHistoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.almaHistory).toMatchObject(almaHistorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        almaHistoryServiceStub.find.resolves(almaHistorySample);
        const wrapper = shallowMount(AlmaHistoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
