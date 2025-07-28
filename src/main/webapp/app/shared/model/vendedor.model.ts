import { IAreaDeNegocio } from 'app/shared/model/area-de-negocio.model';

export interface IVendedor {
  id?: number;
  nombre?: string;
  autorizado?: boolean | null;
  areaDeNegocio?: IAreaDeNegocio | null;
}

export const defaultValue: Readonly<IVendedor> = {
  autorizado: false,
};
