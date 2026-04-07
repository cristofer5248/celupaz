export interface IPrivilege {
  id?: number;
  name?: string;
}

export class Privilege implements IPrivilege {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
