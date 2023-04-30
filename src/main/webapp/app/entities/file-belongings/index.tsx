import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FileBelongings from './file-belongings';
import FileBelongingsDetail from './file-belongings-detail';
import FileBelongingsUpdate from './file-belongings-update';
import FileBelongingsDeleteDialog from './file-belongings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FileBelongingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FileBelongingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FileBelongingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={FileBelongings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FileBelongingsDeleteDialog} />
  </>
);

export default Routes;
