import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tarea from './tarea';
import TareaDetail from './tarea-detail';
import TareaUpdate from './tarea-update';
import TareaDeleteDialog from './tarea-delete-dialog';

const TareaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tarea />} />
    <Route path="new" element={<TareaUpdate />} />
    <Route path=":id">
      <Route index element={<TareaDetail />} />
      <Route path="edit" element={<TareaUpdate />} />
      <Route path="delete" element={<TareaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TareaRoutes;
