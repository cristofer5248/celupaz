import { type ICell } from '@/shared/model/cell.model';
import { type IMember } from '@/shared/model/member.model';

export interface IMemberCelula {
  id?: number;
  fechaCreada?: Date;
  enabled?: boolean;
  member?: IMember | null;
  cell?: ICell | null;
}

export class MemberCelula implements IMemberCelula {
  constructor(
    public id?: number,
    public fechaCreada?: Date,
    public enabled?: boolean,
    public member?: IMember | null,
    public cell?: ICell | null,
  ) {
    this.enabled = this.enabled ?? false;
  }
}
