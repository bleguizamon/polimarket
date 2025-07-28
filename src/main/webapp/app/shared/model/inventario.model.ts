import { IProducto } from 'app/shared/model/producto.model';

export interface IInventario {
  id?: number;
  cantidad?: number | null;
  ubicacion?: string | null;
  producto?: IProducto | null;
}

export const defaultValue: Readonly<IInventario> = {};
