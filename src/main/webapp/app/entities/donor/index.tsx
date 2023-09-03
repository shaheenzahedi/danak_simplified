import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Donor from './donor';
import DonorDetail from './donor-detail';
import DonorUpdate from './donor-update';
import DonorDeleteDialog from './donor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DonorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DonorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DonorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Donor} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DonorDeleteDialog} />
  </>
);

export default Routes;
