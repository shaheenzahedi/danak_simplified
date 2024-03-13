import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CenterImage from './center-image';
import CenterImageDetail from './center-image-detail';
import CenterImageUpdate from './center-image-update';
import CenterImageDeleteDialog from './center-image-delete-dialog';

const CenterImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CenterImage />} />
    <Route path="new" element={<CenterImageUpdate />} />
    <Route path=":id">
      <Route index element={<CenterImageDetail />} />
      <Route path="edit" element={<CenterImageUpdate />} />
      <Route path="delete" element={<CenterImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CenterImageRoutes;
