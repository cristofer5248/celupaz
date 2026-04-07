import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlmaService from '@/entities/alma/alma.service';
import CellService from '@/entities/cell/cell.service';
import RolCelulaService from '@/entities/rol-celula/rol-celula.service';
import AlertService from '@/shared/alert/alert.service';

import AlmaHistoryUpdate from './alma-history-update.vue';
import AlmaHistoryService from './alma-history.service';

type AlmaHistoryUpdateComponentType = InstanceType<typeof AlmaHistoryUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const almaHistorySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AlmaHistoryUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AlmaHistory Management Update Component', () => {
    let comp: AlmaHistoryUpdateComponentType;
    let almaHistoryServiceStub: SinonStubbedInstance<AlmaHistoryService>;

    beforeEach(() => {
      route = {};
      almaHistoryServiceStub = sinon.createStubInstance<AlmaHistoryService>(AlmaHistoryService);
      almaHistoryServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          almaHistoryService: () => almaHistoryServiceStub,
          almaService: () =>
            sinon.createStubInstance<AlmaService>(AlmaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          cellService: () =>
            sinon.createStubInstance<CellService>(CellService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          rolCelulaService: () =>
            sinon.createStubInstance<RolCelulaService>(RolCelulaService, {
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
        const wrapper = shallowMount(AlmaHistoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.almaHistory = almaHistorySample;
        almaHistoryServiceStub.update.resolves(almaHistorySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(almaHistoryServiceStub.update.calledWith(almaHistorySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        almaHistoryServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AlmaHistoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.almaHistory = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(almaHistoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        almaHistoryServiceStub.find.resolves(almaHistorySample);
        almaHistoryServiceStub.retrieve.resolves([almaHistorySample]);

        // WHEN
        route = {
          params: {
            almaHistoryId: `${almaHistorySample.id}`,
          },
        };
        const wrapper = shallowMount(AlmaHistoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.almaHistory).toMatchObject(almaHistorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        almaHistoryServiceStub.find.resolves(almaHistorySample);
        const wrapper = shallowMount(AlmaHistoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
