import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TabletWatchList from './tablet-watch-list';
import TabletWatchListDetail from './tablet-watch-list-detail';
import TabletWatchListUpdate from './tablet-watch-list-update';
import TabletWatchListDeleteDialog from './tablet-watch-list-delete-dialog';

const TabletWatchListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TabletWatchList />} />
    <Route path="new" element={<TabletWatchListUpdate />} />
    <Route path=":id">
      <Route index element={<TabletWatchListDetail />} />
      <Route path="edit" element={<TabletWatchListUpdate />} />
      <Route path="delete" element={<TabletWatchListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletWatchListRoutes;
