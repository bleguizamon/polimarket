import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Proveedor from './proveedor';
import ProveedorDetail from './proveedor-detail';
import ProveedorUpdate from './proveedor-update';
import ProveedorDeleteDialog from './proveedor-delete-dialog';

const ProveedorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Proveedor />} />
    <Route path="new" element={<ProveedorUpdate />} />
    <Route path=":id">
      <Route index element={<ProveedorDetail />} />
      <Route path="edit" element={<ProveedorUpdate />} />
      <Route path="delete" element={<ProveedorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProveedorRoutes;
