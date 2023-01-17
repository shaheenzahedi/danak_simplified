import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserActivity from './user-activity';
import UserActivityDetail from './user-activity-detail';
import UserActivityUpdate from './user-activity-update';
import UserActivityDeleteDialog from './user-activity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserActivityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserActivityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserActivityDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserActivity} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserActivityDeleteDialog} />
  </>
);

export default Routes;
