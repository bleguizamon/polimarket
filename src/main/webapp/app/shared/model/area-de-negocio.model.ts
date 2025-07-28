export interface IAreaDeNegocio {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export const defaultValue: Readonly<IAreaDeNegocio> = {};
