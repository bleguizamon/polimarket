import dayjs from 'dayjs';
import { IProducto } from 'app/shared/model/producto.model';
import { IInventario } from 'app/shared/model/inventario.model';

export interface IMovimientoStock {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  tipo?: string | null;
  cantidad?: number | null;
  referencia?: string | null;
  producto?: IProducto | null;
  inventario?: IInventario | null;
}

export const defaultValue: Readonly<IMovimientoStock> = {};
