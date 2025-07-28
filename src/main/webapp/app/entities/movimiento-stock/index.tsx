import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MovimientoStock from './movimiento-stock';
import MovimientoStockDetail from './movimiento-stock-detail';
import MovimientoStockUpdate from './movimiento-stock-update';
import MovimientoStockDeleteDialog from './movimiento-stock-delete-dialog';

const MovimientoStockRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MovimientoStock />} />
    <Route path="new" element={<MovimientoStockUpdate />} />
    <Route path=":id">
      <Route index element={<MovimientoStockDetail />} />
      <Route path="edit" element={<MovimientoStockUpdate />} />
      <Route path="delete" element={<MovimientoStockDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MovimientoStockRoutes;
