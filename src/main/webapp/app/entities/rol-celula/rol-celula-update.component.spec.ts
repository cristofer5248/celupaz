import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import RolCelulaUpdate from './rol-celula-update.vue';
import RolCelulaService from './rol-celula.service';

type RolCelulaUpdateComponentType = InstanceType<typeof RolCelulaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const rolCelulaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<RolCelulaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('RolCelula Management Update Component', () => {
    let comp: RolCelulaUpdateComponentType;
    let rolCelulaServiceStub: SinonStubbedInstance<RolCelulaService>;

    beforeEach(() => {
      route = {};
      rolCelulaServiceStub = sinon.createStubInstance<RolCelulaService>(RolCelulaService);
      rolCelulaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          rolCelulaService: () => rolCelulaServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(RolCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.rolCelula = rolCelulaSample;
        rolCelulaServiceStub.update.resolves(rolCelulaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rolCelulaServiceStub.update.calledWith(rolCelulaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        rolCelulaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(RolCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.rolCelula = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(rolCelulaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        rolCelulaServiceStub.find.resolves(rolCelulaSample);
        rolCelulaServiceStub.retrieve.resolves([rolCelulaSample]);

        // WHEN
        route = {
          params: {
            rolCelulaId: `${rolCelulaSample.id}`,
          },
        };
        const wrapper = shallowMount(RolCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.rolCelula).toMatchObject(rolCelulaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        rolCelulaServiceStub.find.resolves(rolCelulaSample);
        const wrapper = shallowMount(RolCelulaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
