import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AreaDeNegocio from './area-de-negocio';
import Proveedor from './proveedor';
import Vendedor from './vendedor';
import Cliente from './cliente';
import Producto from './producto';
import Tarea from './tarea';
import Entrega from './entrega';
import Inventario from './inventario';
import MovimientoStock from './movimiento-stock';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="area-de-negocio/*" element={<AreaDeNegocio />} />
        <Route path="proveedor/*" element={<Proveedor />} />
        <Route path="vendedor/*" element={<Vendedor />} />
        <Route path="cliente/*" element={<Cliente />} />
        <Route path="producto/*" element={<Producto />} />
        <Route path="tarea/*" element={<Tarea />} />
        <Route path="entrega/*" element={<Entrega />} />
        <Route path="inventario/*" element={<Inventario />} />
        <Route path="movimiento-stock/*" element={<MovimientoStock />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
