import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TabletLog from './tablet-log';
import TabletLogDetail from './tablet-log-detail';
import TabletLogUpdate from './tablet-log-update';
import TabletLogDeleteDialog from './tablet-log-delete-dialog';

const TabletLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TabletLog />} />
    <Route path="new" element={<TabletLogUpdate />} />
    <Route path=":id">
      <Route index element={<TabletLogDetail />} />
      <Route path="edit" element={<TabletLogUpdate />} />
      <Route path="delete" element={<TabletLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletLogRoutes;
