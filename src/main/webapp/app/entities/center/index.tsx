import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Center from './center';
import CenterDetail from './center-detail';
import CenterUpdate from './center-update';
import CenterDeleteDialog from './center-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CenterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Center} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CenterDeleteDialog} />
  </>
);

export default Routes;
