import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TabletUser from './tablet-user';
import TabletUserDetail from './tablet-user-detail';
import TabletUserUpdate from './tablet-user-update';
import TabletUserDeleteDialog from './tablet-user-delete-dialog';

const TabletUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TabletUser />} />
    <Route path="new" element={<TabletUserUpdate />} />
    <Route path=":id">
      <Route index element={<TabletUserDetail />} />
      <Route path="edit" element={<TabletUserUpdate />} />
      <Route path="delete" element={<TabletUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletUserRoutes;
