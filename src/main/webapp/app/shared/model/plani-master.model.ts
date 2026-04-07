export interface IPlaniMaster {
  id?: number;
  fecha?: Date;
  ofrenda?: number | null;
  visitaCordinador?: boolean | null;
  visitaTutor?: boolean | null;
  visitaDirector?: boolean | null;
  otraVisita?: boolean | null;
  note?: string | null;
  doneby?: string | null;
  completado?: boolean | null;
}

export class PlaniMaster implements IPlaniMaster {
  constructor(
    public id?: number,
    public fecha?: Date,
    public ofrenda?: number | null,
    public visitaCordinador?: boolean | null,
    public visitaTutor?: boolean | null,
    public visitaDirector?: boolean | null,
    public otraVisita?: boolean | null,
    public note?: string | null,
    public doneby?: string | null,
    public completado?: boolean | null,
  ) {
    this.visitaCordinador = this.visitaCordinador ?? false;
    this.visitaTutor = this.visitaTutor ?? false;
    this.visitaDirector = this.visitaDirector ?? false;
    this.otraVisita = this.otraVisita ?? false;
    this.completado = this.completado ?? false;
  }
}
