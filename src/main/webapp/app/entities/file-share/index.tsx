import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FileShare from './file-share';
import FileShareDetail from './file-share-detail';
import FileShareUpdate from './file-share-update';
import FileShareDeleteDialog from './file-share-delete-dialog';

const FileShareRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FileShare />} />
    <Route path="new" element={<FileShareUpdate />} />
    <Route path=":id">
      <Route index element={<FileShareDetail />} />
      <Route path="edit" element={<FileShareUpdate />} />
      <Route path="delete" element={<FileShareDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FileShareRoutes;
