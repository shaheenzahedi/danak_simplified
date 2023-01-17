import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TabletUser from './tablet-user';
import TabletUserDetail from './tablet-user-detail';
import TabletUserUpdate from './tablet-user-update';
import TabletUserDeleteDialog from './tablet-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TabletUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TabletUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TabletUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={TabletUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TabletUserDeleteDialog} />
  </>
);

export default Routes;
