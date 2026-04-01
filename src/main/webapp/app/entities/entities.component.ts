import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';

import AlmaService from './alma/alma.service';
import AlmaHistoryService from './alma-history/alma-history.service';
import AttendanceService from './attendance/attendance.service';
import CellService from './cell/cell.service';
import CellTypeService from './cell-type/cell-type.service';
import IglesiaService from './iglesia/iglesia.service';
import MemberService from './member/member.service';
import MemberCelulaService from './member-celula/member-celula.service';
import PlaniMasterService from './plani-master/plani-master.service';
import PlanificacionService from './planificacion/planificacion.service';
import PrivilegeService from './privilege/privilege.service';
import RolCelulaService from './rol-celula/rol-celula.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('almaService', () => new AlmaService());
    provide('cellTypeService', () => new CellTypeService());
    provide('cellService', () => new CellService());
    provide('memberService', () => new MemberService());
    provide('iglesiaService', () => new IglesiaService());
    provide('privilegeService', () => new PrivilegeService());
    provide('almaHistoryService', () => new AlmaHistoryService());
    provide('attendanceService', () => new AttendanceService());
    provide('planificacionService', () => new PlanificacionService());
    provide('memberCelulaService', () => new MemberCelulaService());
    provide('planiMasterService', () => new PlaniMasterService());
    provide('rolCelulaService', () => new RolCelulaService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
