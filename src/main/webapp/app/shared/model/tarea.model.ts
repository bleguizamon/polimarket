import { IVendedor } from 'app/shared/model/vendedor.model';

export interface ITarea {
  id?: number;
  descripcion?: string | null;
  estado?: string | null;
  vendedor?: IVendedor | null;
}

export const defaultValue: Readonly<ITarea> = {};
