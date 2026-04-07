export interface IAlma {
  id?: number;
  name?: string;
  email?: string | null;
  phone?: string | null;
  department?: string | null;
  municipality?: string | null;
  colony?: string | null;
  description?: string | null;
  fotoContentType?: string | null;
  foto?: string | null;
}

export class Alma implements IAlma {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string | null,
    public phone?: string | null,
    public department?: string | null,
    public municipality?: string | null,
    public colony?: string | null,
    public description?: string | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
  ) {}
}
