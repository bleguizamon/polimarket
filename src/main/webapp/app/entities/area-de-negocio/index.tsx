import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AreaDeNegocio from './area-de-negocio';
import AreaDeNegocioDetail from './area-de-negocio-detail';
import AreaDeNegocioUpdate from './area-de-negocio-update';
import AreaDeNegocioDeleteDialog from './area-de-negocio-delete-dialog';

const AreaDeNegocioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AreaDeNegocio />} />
    <Route path="new" element={<AreaDeNegocioUpdate />} />
    <Route path=":id">
      <Route index element={<AreaDeNegocioDetail />} />
      <Route path="edit" element={<AreaDeNegocioUpdate />} />
      <Route path="delete" element={<AreaDeNegocioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AreaDeNegocioRoutes;
