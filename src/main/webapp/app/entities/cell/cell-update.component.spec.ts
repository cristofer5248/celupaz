import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CellTypeService from '@/entities/cell-type/cell-type.service';
import AlertService from '@/shared/alert/alert.service';

import CellUpdate from './cell-update.vue';
import CellService from './cell.service';

type CellUpdateComponentType = InstanceType<typeof CellUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cellSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CellUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cell Management Update Component', () => {
    let comp: CellUpdateComponentType;
    let cellServiceStub: SinonStubbedInstance<CellService>;

    beforeEach(() => {
      route = {};
      cellServiceStub = sinon.createStubInstance<CellService>(CellService);
      cellServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cellService: () => cellServiceStub,
          cellTypeService: () =>
            sinon.createStubInstance<CellTypeService>(CellTypeService, {
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
        const wrapper = shallowMount(CellUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cell = cellSample;
        cellServiceStub.update.resolves(cellSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cellServiceStub.update.calledWith(cellSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cellServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CellUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cell = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cellServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cellServiceStub.find.resolves(cellSample);
        cellServiceStub.retrieve.resolves([cellSample]);

        // WHEN
        route = {
          params: {
            cellId: `${cellSample.id}`,
          },
        };
        const wrapper = shallowMount(CellUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cell).toMatchObject(cellSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cellServiceStub.find.resolves(cellSample);
        const wrapper = shallowMount(CellUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
