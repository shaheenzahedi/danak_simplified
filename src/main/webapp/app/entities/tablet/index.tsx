import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tablet from './tablet';
import TabletDetail from './tablet-detail';
import TabletUpdate from './tablet-update';
import TabletDeleteDialog from './tablet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TabletUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TabletUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TabletDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tablet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TabletDeleteDialog} />
  </>
);

export default Routes;
