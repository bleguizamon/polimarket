import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Entrega from './entrega';
import EntregaDetail from './entrega-detail';
import EntregaUpdate from './entrega-update';
import EntregaDeleteDialog from './entrega-delete-dialog';

const EntregaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Entrega />} />
    <Route path="new" element={<EntregaUpdate />} />
    <Route path=":id">
      <Route index element={<EntregaDetail />} />
      <Route path="edit" element={<EntregaUpdate />} />
      <Route path="delete" element={<EntregaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EntregaRoutes;
