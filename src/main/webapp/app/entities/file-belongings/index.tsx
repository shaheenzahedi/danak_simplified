import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FileBelongings from './file-belongings';
import FileBelongingsDetail from './file-belongings-detail';
import FileBelongingsUpdate from './file-belongings-update';
import FileBelongingsDeleteDialog from './file-belongings-delete-dialog';

const FileBelongingsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FileBelongings />} />
    <Route path="new" element={<FileBelongingsUpdate />} />
    <Route path=":id">
      <Route index element={<FileBelongingsDetail />} />
      <Route path="edit" element={<FileBelongingsUpdate />} />
      <Route path="delete" element={<FileBelongingsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FileBelongingsRoutes;
