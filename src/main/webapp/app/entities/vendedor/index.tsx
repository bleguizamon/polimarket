import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vendedor from './vendedor';
import VendedorDetail from './vendedor-detail';
import VendedorUpdate from './vendedor-update';
import VendedorDeleteDialog from './vendedor-delete-dialog';

const VendedorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vendedor />} />
    <Route path="new" element={<VendedorUpdate />} />
    <Route path=":id">
      <Route index element={<VendedorDetail />} />
      <Route path="edit" element={<VendedorUpdate />} />
      <Route path="delete" element={<VendedorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VendedorRoutes;
