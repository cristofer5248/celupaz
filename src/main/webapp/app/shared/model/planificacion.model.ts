import { type IAlmaHistory } from '@/shared/model/alma-history.model';
import { type IPlaniMaster } from '@/shared/model/plani-master.model';
import { type IPrivilege } from '@/shared/model/privilege.model';

export interface IPlanificacion {
  id?: number;
  fecha?: Date;
  almahistory?: IAlmaHistory | null;
  privilege?: IPrivilege | null;
  planiMaster?: IPlaniMaster | null;
}

export class Planificacion implements IPlanificacion {
  constructor(
    public id?: number,
    public fecha?: Date,
    public almahistory?: IAlmaHistory | null,
    public privilege?: IPrivilege | null,
    public planiMaster?: IPlaniMaster | null,
  ) {}
}
