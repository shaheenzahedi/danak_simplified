import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PanelLog from './panel-log';
import PanelLogDetail from './panel-log-detail';
import PanelLogUpdate from './panel-log-update';
import PanelLogDeleteDialog from './panel-log-delete-dialog';

const PanelLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PanelLog />} />
    <Route path="new" element={<PanelLogUpdate />} />
    <Route path=":id">
      <Route index element={<PanelLogDetail />} />
      <Route path="edit" element={<PanelLogUpdate />} />
      <Route path="delete" element={<PanelLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PanelLogRoutes;
