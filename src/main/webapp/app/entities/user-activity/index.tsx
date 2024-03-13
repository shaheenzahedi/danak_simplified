import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserActivity from './user-activity';
import UserActivityDetail from './user-activity-detail';
import UserActivityUpdate from './user-activity-update';
import UserActivityDeleteDialog from './user-activity-delete-dialog';

const UserActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserActivity />} />
    <Route path="new" element={<UserActivityUpdate />} />
    <Route path=":id">
      <Route index element={<UserActivityDetail />} />
      <Route path="edit" element={<UserActivityUpdate />} />
      <Route path="delete" element={<UserActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserActivityRoutes;
