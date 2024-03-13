import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DonorImage from './donor-image';
import DonorImageDetail from './donor-image-detail';
import DonorImageUpdate from './donor-image-update';
import DonorImageDeleteDialog from './donor-image-delete-dialog';

const DonorImageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DonorImage />} />
    <Route path="new" element={<DonorImageUpdate />} />
    <Route path=":id">
      <Route index element={<DonorImageDetail />} />
      <Route path="edit" element={<DonorImageUpdate />} />
      <Route path="delete" element={<DonorImageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DonorImageRoutes;
