import { type ICellType } from '@/shared/model/cell-type.model';

export interface ICell {
  id?: number;
  name?: string | null;
  startDate?: Date | null;
  description?: string | null;
  sector?: number | null;
  lider?: string | null;
  cordinador?: string | null;
  cellType?: ICellType | null;
}

export class Cell implements ICell {
  constructor(
    public id?: number,
    public name?: string | null,
    public startDate?: Date | null,
    public description?: string | null,
    public sector?: number | null,
    public lider?: string | null,
    public cordinador?: string | null,
    public cellType?: ICellType | null,
  ) {}
}
