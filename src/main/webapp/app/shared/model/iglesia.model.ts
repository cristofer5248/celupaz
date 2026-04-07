export interface IIglesia {
  id?: number;
  name?: string;
}

export class Iglesia implements IIglesia {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
