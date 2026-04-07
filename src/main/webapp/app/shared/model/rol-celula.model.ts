export interface IRolCelula {
  id?: number;
  name?: string;
}

export class RolCelula implements IRolCelula {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
