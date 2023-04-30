import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Version from './version';
import VersionDetail from './version-detail';
import VersionUpdate from './version-update';
import VersionDeleteDialog from './version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Version} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VersionDeleteDialog} />
  </>
);

export default Routes;
