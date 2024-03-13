import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TabletUserWatchList from './tablet-user-watch-list';
import TabletUserWatchListDetail from './tablet-user-watch-list-detail';
import TabletUserWatchListUpdate from './tablet-user-watch-list-update';
import TabletUserWatchListDeleteDialog from './tablet-user-watch-list-delete-dialog';

const TabletUserWatchListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TabletUserWatchList />} />
    <Route path="new" element={<TabletUserWatchListUpdate />} />
    <Route path=":id">
      <Route index element={<TabletUserWatchListDetail />} />
      <Route path="edit" element={<TabletUserWatchListUpdate />} />
      <Route path="delete" element={<TabletUserWatchListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TabletUserWatchListRoutes;
