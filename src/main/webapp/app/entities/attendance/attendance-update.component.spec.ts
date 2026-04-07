import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MemberCelulaService from '@/entities/member-celula/member-celula.service';
import PlanificacionService from '@/entities/planificacion/planificacion.service';
import AlertService from '@/shared/alert/alert.service';

import AttendanceUpdate from './attendance-update.vue';
import AttendanceService from './attendance.service';

type AttendanceUpdateComponentType = InstanceType<typeof AttendanceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const attendanceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AttendanceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Attendance Management Update Component', () => {
    let comp: AttendanceUpdateComponentType;
    let attendanceServiceStub: SinonStubbedInstance<AttendanceService>;

    beforeEach(() => {
      route = {};
      attendanceServiceStub = sinon.createStubInstance<AttendanceService>(AttendanceService);
      attendanceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          attendanceService: () => attendanceServiceStub,
          memberCelulaService: () =>
            sinon.createStubInstance<MemberCelulaService>(MemberCelulaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          planificacionService: () =>
            sinon.createStubInstance<PlanificacionService>(PlanificacionService, {
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
        const wrapper = shallowMount(AttendanceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.attendance = attendanceSample;
        attendanceServiceStub.update.resolves(attendanceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attendanceServiceStub.update.calledWith(attendanceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        attendanceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AttendanceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.attendance = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attendanceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        attendanceServiceStub.find.resolves(attendanceSample);
        attendanceServiceStub.retrieve.resolves([attendanceSample]);

        // WHEN
        route = {
          params: {
            attendanceId: `${attendanceSample.id}`,
          },
        };
        const wrapper = shallowMount(AttendanceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.attendance).toMatchObject(attendanceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        attendanceServiceStub.find.resolves(attendanceSample);
        const wrapper = shallowMount(AttendanceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
