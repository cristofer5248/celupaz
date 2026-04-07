import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PrivilegeUpdate from './privilege-update.vue';
import PrivilegeService from './privilege.service';

type PrivilegeUpdateComponentType = InstanceType<typeof PrivilegeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const privilegeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PrivilegeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Privilege Management Update Component', () => {
    let comp: PrivilegeUpdateComponentType;
    let privilegeServiceStub: SinonStubbedInstance<PrivilegeService>;

    beforeEach(() => {
      route = {};
      privilegeServiceStub = sinon.createStubInstance<PrivilegeService>(PrivilegeService);
      privilegeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          privilegeService: () => privilegeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PrivilegeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.privilege = privilegeSample;
        privilegeServiceStub.update.resolves(privilegeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(privilegeServiceStub.update.calledWith(privilegeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        privilegeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PrivilegeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.privilege = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(privilegeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        privilegeServiceStub.find.resolves(privilegeSample);
        privilegeServiceStub.retrieve.resolves([privilegeSample]);

        // WHEN
        route = {
          params: {
            privilegeId: `${privilegeSample.id}`,
          },
        };
        const wrapper = shallowMount(PrivilegeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.privilege).toMatchObject(privilegeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        privilegeServiceStub.find.resolves(privilegeSample);
        const wrapper = shallowMount(PrivilegeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
