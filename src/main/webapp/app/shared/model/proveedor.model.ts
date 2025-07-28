export interface IProveedor {
  id?: number;
  nombre?: string;
  telefono?: string | null;
}

export const defaultValue: Readonly<IProveedor> = {};
