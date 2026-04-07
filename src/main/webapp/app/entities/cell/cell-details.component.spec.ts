import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CellDetails from './cell-details.vue';
import CellService from './cell.service';

type CellDetailsComponentType = InstanceType<typeof CellDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cellSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Cell Management Detail Component', () => {
    let cellServiceStub: SinonStubbedInstance<CellService>;
    let mountOptions: MountingOptions<CellDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cellServiceStub = sinon.createStubInstance<CellService>(CellService);

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
          cellService: () => cellServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cellServiceStub.find.resolves(cellSample);
        route = {
          params: {
            cellId: `${123}`,
          },
        };
        const wrapper = shallowMount(CellDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cell).toMatchObject(cellSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cellServiceStub.find.resolves(cellSample);
        const wrapper = shallowMount(CellDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
