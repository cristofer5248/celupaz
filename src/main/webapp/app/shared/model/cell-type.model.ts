export interface ICellType {
  id?: number;
  name?: string;
}

export class CellType implements ICellType {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
