import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Donor from './donor';
import DonorDetail from './donor-detail';
import DonorUpdate from './donor-update';
import DonorDeleteDialog from './donor-delete-dialog';

const DonorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Donor />} />
    <Route path="new" element={<DonorUpdate />} />
    <Route path=":id">
      <Route index element={<DonorDetail />} />
      <Route path="edit" element={<DonorUpdate />} />
      <Route path="delete" element={<DonorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DonorRoutes;
