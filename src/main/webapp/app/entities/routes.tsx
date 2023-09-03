import React from 'react';
import {Route, Switch} from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tablet from './tablet';
import TabletUser from './tablet-user';
import UserActivity from './user-activity';
import File from './file';
import FileBelongings from './file-belongings';
import Version from './version';
import Donor from './donor';
import Center from './center';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}tablet`} component={Tablet} />
        <ErrorBoundaryRoute path={`${match.url}tablet-user`} component={TabletUser} />
        <ErrorBoundaryRoute path={`${match.url}user-activity`} component={UserActivity} />
        <ErrorBoundaryRoute path={`${match.url}file`} component={File} />
        <ErrorBoundaryRoute path={`${match.url}file-belongings`} component={FileBelongings} />
        <ErrorBoundaryRoute path={`${match.url}version`} component={Version} />
        <ErrorBoundaryRoute path={`${match.url}donor`} component={Donor} />
        <ErrorBoundaryRoute path={`${match.url}center`} component={Center} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
