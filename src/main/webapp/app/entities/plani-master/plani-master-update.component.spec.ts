import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PlaniMasterUpdate from './plani-master-update.vue';
import PlaniMasterService from './plani-master.service';

type PlaniMasterUpdateComponentType = InstanceType<typeof PlaniMasterUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planiMasterSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PlaniMasterUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PlaniMaster Management Update Component', () => {
    let comp: PlaniMasterUpdateComponentType;
    let planiMasterServiceStub: SinonStubbedInstance<PlaniMasterService>;

    beforeEach(() => {
      route = {};
      planiMasterServiceStub = sinon.createStubInstance<PlaniMasterService>(PlaniMasterService);
      planiMasterServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          planiMasterService: () => planiMasterServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PlaniMasterUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planiMaster = planiMasterSample;
        planiMasterServiceStub.update.resolves(planiMasterSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planiMasterServiceStub.update.calledWith(planiMasterSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        planiMasterServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PlaniMasterUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planiMaster = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planiMasterServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        planiMasterServiceStub.find.resolves(planiMasterSample);
        planiMasterServiceStub.retrieve.resolves([planiMasterSample]);

        // WHEN
        route = {
          params: {
            planiMasterId: `${planiMasterSample.id}`,
          },
        };
        const wrapper = shallowMount(PlaniMasterUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.planiMaster).toMatchObject(planiMasterSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planiMasterServiceStub.find.resolves(planiMasterSample);
        const wrapper = shallowMount(PlaniMasterUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
