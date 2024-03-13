import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TabletUserImage from './tablet-user-image';
import TabletUserImageDetail from './tablet-user-image-detail';
import TabletUserImageUpdate from './tablet-user-image-update';
import TabletUserImageDeleteDialog from './tablet-user-image-delete-dialog';

const TabletUserImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TabletUserImage />} />
    <Route path="new" element={<TabletUserImageUpdate />} />
    <Route path=":id">
      <Route index element={<TabletUserImageDetail />} />
      <Route path="edit" element={<TabletUserImageUpdate />} />
      <Route path="delete" element={<TabletUserImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletUserImageRoutes;
