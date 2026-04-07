import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AttendanceDetails from './attendance-details.vue';
import AttendanceService from './attendance.service';

type AttendanceDetailsComponentType = InstanceType<typeof AttendanceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const attendanceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Attendance Management Detail Component', () => {
    let attendanceServiceStub: SinonStubbedInstance<AttendanceService>;
    let mountOptions: MountingOptions<AttendanceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      attendanceServiceStub = sinon.createStubInstance<AttendanceService>(AttendanceService);

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
          attendanceService: () => attendanceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        attendanceServiceStub.find.resolves(attendanceSample);
        route = {
          params: {
            attendanceId: `${123}`,
          },
        };
        const wrapper = shallowMount(AttendanceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.attendance).toMatchObject(attendanceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        attendanceServiceStub.find.resolves(attendanceSample);
        const wrapper = shallowMount(AttendanceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
