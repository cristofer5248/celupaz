import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import IglesiaUpdate from './iglesia-update.vue';
import IglesiaService from './iglesia.service';

type IglesiaUpdateComponentType = InstanceType<typeof IglesiaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const iglesiaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<IglesiaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Iglesia Management Update Component', () => {
    let comp: IglesiaUpdateComponentType;
    let iglesiaServiceStub: SinonStubbedInstance<IglesiaService>;

    beforeEach(() => {
      route = {};
      iglesiaServiceStub = sinon.createStubInstance<IglesiaService>(IglesiaService);
      iglesiaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          iglesiaService: () => iglesiaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(IglesiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.iglesia = iglesiaSample;
        iglesiaServiceStub.update.resolves(iglesiaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iglesiaServiceStub.update.calledWith(iglesiaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        iglesiaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(IglesiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.iglesia = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(iglesiaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        iglesiaServiceStub.find.resolves(iglesiaSample);
        iglesiaServiceStub.retrieve.resolves([iglesiaSample]);

        // WHEN
        route = {
          params: {
            iglesiaId: `${iglesiaSample.id}`,
          },
        };
        const wrapper = shallowMount(IglesiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.iglesia).toMatchObject(iglesiaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        iglesiaServiceStub.find.resolves(iglesiaSample);
        const wrapper = shallowMount(IglesiaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
