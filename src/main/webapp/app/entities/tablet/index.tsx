import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tablet from './tablet';
import TabletDetail from './tablet-detail';
import TabletUpdate from './tablet-update';
import TabletDeleteDialog from './tablet-delete-dialog';

const TabletRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tablet />} />
    <Route path="new" element={<TabletUpdate />} />
    <Route path=":id">
      <Route index element={<TabletDetail />} />
      <Route path="edit" element={<TabletUpdate />} />
      <Route path="delete" element={<TabletDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletRoutes;
