import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CellTypeUpdate from './cell-type-update.vue';
import CellTypeService from './cell-type.service';

type CellTypeUpdateComponentType = InstanceType<typeof CellTypeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cellTypeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CellTypeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CellType Management Update Component', () => {
    let comp: CellTypeUpdateComponentType;
    let cellTypeServiceStub: SinonStubbedInstance<CellTypeService>;

    beforeEach(() => {
      route = {};
      cellTypeServiceStub = sinon.createStubInstance<CellTypeService>(CellTypeService);
      cellTypeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cellTypeService: () => cellTypeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CellTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cellType = cellTypeSample;
        cellTypeServiceStub.update.resolves(cellTypeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cellTypeServiceStub.update.calledWith(cellTypeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cellTypeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CellTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cellType = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cellTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cellTypeServiceStub.find.resolves(cellTypeSample);
        cellTypeServiceStub.retrieve.resolves([cellTypeSample]);

        // WHEN
        route = {
          params: {
            cellTypeId: `${cellTypeSample.id}`,
          },
        };
        const wrapper = shallowMount(CellTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cellType).toMatchObject(cellTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cellTypeServiceStub.find.resolves(cellTypeSample);
        const wrapper = shallowMount(CellTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
