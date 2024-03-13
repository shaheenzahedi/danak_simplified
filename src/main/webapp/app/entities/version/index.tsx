import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Version from './version';
import VersionDetail from './version-detail';
import VersionUpdate from './version-update';
import VersionDeleteDialog from './version-delete-dialog';

const VersionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Version />} />
    <Route path="new" element={<VersionUpdate />} />
    <Route path=":id">
      <Route index element={<VersionDetail />} />
      <Route path="edit" element={<VersionUpdate />} />
      <Route path="delete" element={<VersionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VersionRoutes;
