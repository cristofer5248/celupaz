import { Authority } from '@/shared/jhipster/constants';
const Entities = () => import('@/entities/entities.vue');

const Alma = () => import('@/entities/alma/alma.vue');
const AlmaUpdate = () => import('@/entities/alma/alma-update.vue');
const AlmaDetails = () => import('@/entities/alma/alma-details.vue');

const CellType = () => import('@/entities/cell-type/cell-type.vue');
const CellTypeUpdate = () => import('@/entities/cell-type/cell-type-update.vue');
const CellTypeDetails = () => import('@/entities/cell-type/cell-type-details.vue');

const Cell = () => import('@/entities/cell/cell.vue');
const CellUpdate = () => import('@/entities/cell/cell-update.vue');
const CellDetails = () => import('@/entities/cell/cell-details.vue');

const Member = () => import('@/entities/member/member.vue');
const MemberUpdate = () => import('@/entities/member/member-update.vue');
const MemberDetails = () => import('@/entities/member/member-details.vue');

const Iglesia = () => import('@/entities/iglesia/iglesia.vue');
const IglesiaUpdate = () => import('@/entities/iglesia/iglesia-update.vue');
const IglesiaDetails = () => import('@/entities/iglesia/iglesia-details.vue');

const Privilege = () => import('@/entities/privilege/privilege.vue');
const PrivilegeUpdate = () => import('@/entities/privilege/privilege-update.vue');
const PrivilegeDetails = () => import('@/entities/privilege/privilege-details.vue');

const AlmaHistory = () => import('@/entities/alma-history/alma-history.vue');
const AlmaHistoryUpdate = () => import('@/entities/alma-history/alma-history-update.vue');
const AlmaHistoryDetails = () => import('@/entities/alma-history/alma-history-details.vue');

const Attendance = () => import('@/entities/attendance/attendance.vue');
const AttendanceUpdate = () => import('@/entities/attendance/attendance-update.vue');
const AttendanceDetails = () => import('@/entities/attendance/attendance-details.vue');

const Planificacion = () => import('@/entities/planificacion/planificacion.vue');
const PlanificacionUpdate = () => import('@/entities/planificacion/planificacion-update.vue');
const PlanificacionDetails = () => import('@/entities/planificacion/planificacion-details.vue');

const MemberCelula = () => import('@/entities/member-celula/member-celula.vue');
const MemberCelulaUpdate = () => import('@/entities/member-celula/member-celula-update.vue');
const MemberCelulaDetails = () => import('@/entities/member-celula/member-celula-details.vue');

const PlaniMaster = () => import('@/entities/plani-master/plani-master.vue');
const PlaniMasterUpdate = () => import('@/entities/plani-master/plani-master-update.vue');
const PlaniMasterDetails = () => import('@/entities/plani-master/plani-master-details.vue');

const RolCelula = () => import('@/entities/rol-celula/rol-celula.vue');
const RolCelulaUpdate = () => import('@/entities/rol-celula/rol-celula-update.vue');
const RolCelulaDetails = () => import('@/entities/rol-celula/rol-celula-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'alma',
      name: 'Alma',
      component: Alma,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma/new',
      name: 'AlmaCreate',
      component: AlmaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma/:almaId/edit',
      name: 'AlmaEdit',
      component: AlmaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma/:almaId/view',
      name: 'AlmaView',
      component: AlmaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cell-type',
      name: 'CellType',
      component: CellType,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell-type/new',
      name: 'CellTypeCreate',
      component: CellTypeUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell-type/:cellTypeId/edit',
      name: 'CellTypeEdit',
      component: CellTypeUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell-type/:cellTypeId/view',
      name: 'CellTypeView',
      component: CellTypeDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell',
      name: 'Cell',
      component: Cell,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell/new',
      name: 'CellCreate',
      component: CellUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell/:cellId/edit',
      name: 'CellEdit',
      component: CellUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'cell/:cellId/view',
      name: 'CellView',
      component: CellDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'member',
      name: 'Member',
      component: Member,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member/new',
      name: 'MemberCreate',
      component: MemberUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member/:memberId/edit',
      name: 'MemberEdit',
      component: MemberUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member/:memberId/view',
      name: 'MemberView',
      component: MemberDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'iglesia',
      name: 'Iglesia',
      component: Iglesia,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'iglesia/new',
      name: 'IglesiaCreate',
      component: IglesiaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'iglesia/:iglesiaId/edit',
      name: 'IglesiaEdit',
      component: IglesiaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'iglesia/:iglesiaId/view',
      name: 'IglesiaView',
      component: IglesiaDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'privilege',
      name: 'Privilege',
      component: Privilege,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'privilege/new',
      name: 'PrivilegeCreate',
      component: PrivilegeUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'privilege/:privilegeId/edit',
      name: 'PrivilegeEdit',
      component: PrivilegeUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'privilege/:privilegeId/view',
      name: 'PrivilegeView',
      component: PrivilegeDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma-history',
      name: 'AlmaHistory',
      component: AlmaHistory,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma-history/new',
      name: 'AlmaHistoryCreate',
      component: AlmaHistoryUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma-history/:almaHistoryId/edit',
      name: 'AlmaHistoryEdit',
      component: AlmaHistoryUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'alma-history/:almaHistoryId/view',
      name: 'AlmaHistoryView',
      component: AlmaHistoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attendance',
      name: 'Attendance',
      component: Attendance,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attendance/new',
      name: 'AttendanceCreate',
      component: AttendanceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attendance/:attendanceId/edit',
      name: 'AttendanceEdit',
      component: AttendanceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attendance/:attendanceId/view',
      name: 'AttendanceView',
      component: AttendanceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'planificacion',
      name: 'Planificacion',
      component: Planificacion,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'planificacion/new',
      name: 'PlanificacionCreate',
      component: PlanificacionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'planificacion/:planificacionId/edit',
      name: 'PlanificacionEdit',
      component: PlanificacionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'planificacion/:planificacionId/view',
      name: 'PlanificacionView',
      component: PlanificacionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member-celula',
      name: 'MemberCelula',
      component: MemberCelula,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member-celula/new',
      name: 'MemberCelulaCreate',
      component: MemberCelulaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member-celula/:memberCelulaId/edit',
      name: 'MemberCelulaEdit',
      component: MemberCelulaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'member-celula/:memberCelulaId/view',
      name: 'MemberCelulaView',
      component: MemberCelulaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plani-master',
      name: 'PlaniMaster',
      component: PlaniMaster,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plani-master/new',
      name: 'PlaniMasterCreate',
      component: PlaniMasterUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plani-master/:planiMasterId/edit',
      name: 'PlaniMasterEdit',
      component: PlaniMasterUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plani-master/:planiMasterId/view',
      name: 'PlaniMasterView',
      component: PlaniMasterDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'rol-celula',
      name: 'RolCelula',
      component: RolCelula,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'rol-celula/new',
      name: 'RolCelulaCreate',
      component: RolCelulaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'rol-celula/:rolCelulaId/edit',
      name: 'RolCelulaEdit',
      component: RolCelulaUpdate,
      meta: { authorities: [Authority.ADMIN] },
    },
    {
      path: 'rol-celula/:rolCelulaId/view',
      name: 'RolCelulaView',
      component: RolCelulaDetails,
      meta: { authorities: [Authority.ADMIN] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
