import { IProveedor } from 'app/shared/model/proveedor.model';
import { ITarea } from 'app/shared/model/tarea.model';

export interface IProducto {
  id?: number;
  nombre?: string;
  cantidadDisponible?: number | null;
  proveedor?: IProveedor | null;
  tarea?: ITarea | null;
}

export const defaultValue: Readonly<IProducto> = {};
