import React from 'react';
import {Route, Switch} from 'react-router-dom';


import CenterDonor from './center-donor';
import CenterDonorDetail from './center-donor-detail';
import CenterDonorUpdate from './center-donor-update';
import CenterDonorDeleteDialog from './center-donor-delete-dialog';
import ErrorBoundaryRoute from "app/shared/error/error-boundary-route";
const Routes = ({match}) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CenterDonorUpdate}/>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CenterDonorUpdate}/>
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CenterDonorDetail}/>
      <ErrorBoundaryRoute path={match.url} component={CenterDonor}/>
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CenterDonorDeleteDialog}/>
  </>
);

export default Routes;
