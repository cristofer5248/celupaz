import { type IMemberCelula } from '@/shared/model/member-celula.model';
import { type IPlanificacion } from '@/shared/model/planificacion.model';

export interface IAttendance {
  id?: number;
  fecha?: Date;
  membercelula?: IMemberCelula | null;
  planificacion?: IPlanificacion | null;
}

export class Attendance implements IAttendance {
  constructor(
    public id?: number,
    public fecha?: Date,
    public membercelula?: IMemberCelula | null,
    public planificacion?: IPlanificacion | null,
  ) {}
}
