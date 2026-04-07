import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CellTypeDetails from './cell-type-details.vue';
import CellTypeService from './cell-type.service';

type CellTypeDetailsComponentType = InstanceType<typeof CellTypeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cellTypeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CellType Management Detail Component', () => {
    let cellTypeServiceStub: SinonStubbedInstance<CellTypeService>;
    let mountOptions: MountingOptions<CellTypeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cellTypeServiceStub = sinon.createStubInstance<CellTypeService>(CellTypeService);

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
          cellTypeService: () => cellTypeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cellTypeServiceStub.find.resolves(cellTypeSample);
        route = {
          params: {
            cellTypeId: `${123}`,
          },
        };
        const wrapper = shallowMount(CellTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cellType).toMatchObject(cellTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cellTypeServiceStub.find.resolves(cellTypeSample);
        const wrapper = shallowMount(CellTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
