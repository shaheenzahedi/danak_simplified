import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CenterWatchList from './center-watch-list';
import CenterWatchListDetail from './center-watch-list-detail';
import CenterWatchListUpdate from './center-watch-list-update';
import CenterWatchListDeleteDialog from './center-watch-list-delete-dialog';

const CenterWatchListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CenterWatchList />} />
    <Route path="new" element={<CenterWatchListUpdate />} />
    <Route path=":id">
      <Route index element={<CenterWatchListDetail />} />
      <Route path="edit" element={<CenterWatchListUpdate />} />
      <Route path="delete" element={<CenterWatchListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CenterWatchListRoutes;
