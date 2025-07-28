import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Inventario from './inventario';
import InventarioDetail from './inventario-detail';
import InventarioUpdate from './inventario-update';
import InventarioDeleteDialog from './inventario-delete-dialog';

const InventarioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inventario />} />
    <Route path="new" element={<InventarioUpdate />} />
    <Route path=":id">
      <Route index element={<InventarioDetail />} />
      <Route path="edit" element={<InventarioUpdate />} />
      <Route path="delete" element={<InventarioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InventarioRoutes;
