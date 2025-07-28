import { IVendedor } from 'app/shared/model/vendedor.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  contacto?: string | null;
  vendedor?: IVendedor | null;
}

export const defaultValue: Readonly<ICliente> = {};
