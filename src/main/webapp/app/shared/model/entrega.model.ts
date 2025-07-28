import dayjs from 'dayjs';
import { ITarea } from 'app/shared/model/tarea.model';
import { ICliente } from 'app/shared/model/cliente.model';
import { IProducto } from 'app/shared/model/producto.model';

export interface IEntrega {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  estado?: string | null;
  tarea?: ITarea | null;
  cliente?: ICliente | null;
  producto?: IProducto | null;
}

export const defaultValue: Readonly<IEntrega> = {};
