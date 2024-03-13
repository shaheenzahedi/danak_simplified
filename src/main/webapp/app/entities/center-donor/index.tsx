import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CenterDonor from './center-donor';
import CenterDonorDetail from './center-donor-detail';
import CenterDonorUpdate from './center-donor-update';
import CenterDonorDeleteDialog from './center-donor-delete-dialog';

const CenterDonorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CenterDonor />} />
    <Route path="new" element={<CenterDonorUpdate />} />
    <Route path=":id">
      <Route index element={<CenterDonorDetail />} />
      <Route path="edit" element={<CenterDonorUpdate />} />
      <Route path="delete" element={<CenterDonorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CenterDonorRoutes;
