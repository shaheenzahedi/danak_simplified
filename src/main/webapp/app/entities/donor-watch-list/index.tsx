import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DonorWatchList from './donor-watch-list';
import DonorWatchListDetail from './donor-watch-list-detail';
import DonorWatchListUpdate from './donor-watch-list-update';
import DonorWatchListDeleteDialog from './donor-watch-list-delete-dialog';

const DonorWatchListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DonorWatchList />} />
    <Route path="new" element={<DonorWatchListUpdate />} />
    <Route path=":id">
      <Route index element={<DonorWatchListDetail />} />
      <Route path="edit" element={<DonorWatchListUpdate />} />
      <Route path="delete" element={<DonorWatchListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DonorWatchListRoutes;
