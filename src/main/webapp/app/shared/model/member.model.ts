import { type IIglesia } from '@/shared/model/iglesia.model';

export interface IMember {
  id?: number;
  name?: string;
  email?: string | null;
  phone?: string | null;
  department?: string | null;
  municipality?: string | null;
  colony?: string | null;
  isCompaz?: boolean | null;
  fechacumple?: Date | null;
  padre?: boolean | null;
  relacion?: string | null;
  iglesia?: IIglesia | null;
  createdBy?: string | null;
}

export class Member implements IMember {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string | null,
    public phone?: string | null,
    public department?: string | null,
    public municipality?: string | null,
    public colony?: string | null,
    public isCompaz?: boolean | null,
    public fechacumple?: Date | null,
    public padre?: boolean | null,
    public relacion?: string | null,
    public iglesia?: IIglesia | null,
    public createdBy?: createdBy | null,
  ) {
    this.isCompaz = this.isCompaz ?? false;
    this.padre = this.padre ?? false;
  }
}
