import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tablet from './tablet';
import TabletUser from './tablet-user';
import PanelLog from './panel-log';
import TabletLog from './tablet-log';
import TabletUserImage from './tablet-user-image';
import DonorImage from './donor-image';
import CenterImage from './center-image';
import TabletUserWatchList from './tablet-user-watch-list';
import TabletWatchList from './tablet-watch-list';
import CenterWatchList from './center-watch-list';
import DonorWatchList from './donor-watch-list';
import UserActivity from './user-activity';
import UploadedFile from './uploaded-file';
import FileShare from './file-share';
import File from './file';
import FileBelongings from './file-belongings';
import Version from './version';
import Center from './center';
import Donor from './donor';
import CenterDonor from './center-donor';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="tablet/*" element={<Tablet/>}/>
        <Route path="tablet-user/*" element={<TabletUser />} />
        <Route path="panel-log/*" element={<PanelLog />} />
        <Route path="tablet-log/*" element={<TabletLog />} />
        <Route path="tablet-user-image/*" element={<TabletUserImage />} />
        <Route path="donor-image/*" element={<DonorImage />} />
        <Route path="center-image/*" element={<CenterImage />} />
        <Route path="tablet-user-watch-list/*" element={<TabletUserWatchList />} />
        <Route path="tablet-watch-list/*" element={<TabletWatchList />} />
        <Route path="center-watch-list/*" element={<CenterWatchList />} />
        <Route path="donor-watch-list/*" element={<DonorWatchList />} />
        <Route path="user-activity/*" element={<UserActivity />} />
        <Route path="uploaded-file/*" element={<UploadedFile />} />
        <Route path="file-share/*" element={<FileShare />} />
        <Route path="file/*" element={<File />} />
        <Route path="file-belongings/*" element={<FileBelongings />} />
        <Route path="version/*" element={<Version />} />
        <Route path="center/*" element={<Center />} />
        <Route path="donor/*" element={<Donor />} />
        <Route path="center-donor/*" element={<CenterDonor />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
