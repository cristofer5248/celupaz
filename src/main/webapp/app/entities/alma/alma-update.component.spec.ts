import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AlmaUpdate from './alma-update.vue';
import AlmaService from './alma.service';

type AlmaUpdateComponentType = InstanceType<typeof AlmaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const almaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AlmaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Alma Management Update Component', () => {
    let comp: AlmaUpdateComponentType;
    let almaServiceStub: SinonStubbedInstance<AlmaService>;

    beforeEach(() => {
      route = {};
      almaServiceStub = sinon.createStubInstance<AlmaService>(AlmaService);
      almaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          almaService: () => almaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AlmaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alma = almaSample;
        almaServiceStub.update.resolves(almaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(almaServiceStub.update.calledWith(almaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        almaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AlmaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.alma = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(almaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        almaServiceStub.find.resolves(almaSample);
        almaServiceStub.retrieve.resolves([almaSample]);

        // WHEN
        route = {
          params: {
            almaId: `${almaSample.id}`,
          },
        };
        const wrapper = shallowMount(AlmaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.alma).toMatchObject(almaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        almaServiceStub.find.resolves(almaSample);
        const wrapper = shallowMount(AlmaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
