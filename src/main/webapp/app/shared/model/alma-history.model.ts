import { type IAlma } from '@/shared/model/alma.model';
import { type ICell } from '@/shared/model/cell.model';
import { type IRolCelula } from '@/shared/model/rol-celula.model';

export interface IAlmaHistory {
  id?: number;
  fecha?: string | null;
  alma?: IAlma | null;
  cell?: ICell | null;
  rolcelula?: IRolCelula | null;
}

export class AlmaHistory implements IAlmaHistory {
  constructor(
    public id?: number,
    public fecha?: string | null,
    public alma?: IAlma | null,
    public cell?: ICell | null,
    public rolcelula?: IRolCelula | null,
  ) {}
}
