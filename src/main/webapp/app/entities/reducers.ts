import tablet from 'app/entities/tablet/tablet.reducer';
import tabletUser from 'app/entities/tablet-user/tablet-user.reducer';
import panelLog from 'app/entities/panel-log/panel-log.reducer';
import tabletLog from 'app/entities/tablet-log/tablet-log.reducer';
import tabletUserImage from 'app/entities/tablet-user-image/tablet-user-image.reducer';
import donorImage from 'app/entities/donor-image/donor-image.reducer';
import centerImage from 'app/entities/center-image/center-image.reducer';
import tabletUserWatchList from 'app/entities/tablet-user-watch-list/tablet-user-watch-list.reducer';
import tabletWatchList from 'app/entities/tablet-watch-list/tablet-watch-list.reducer';
import centerWatchList from 'app/entities/center-watch-list/center-watch-list.reducer';
import donorWatchList from 'app/entities/donor-watch-list/donor-watch-list.reducer';
import userActivity from 'app/entities/user-activity/user-activity.reducer';
import uploadedFile from 'app/entities/uploaded-file/uploaded-file.reducer';
import fileShare from 'app/entities/file-share/file-share.reducer';
import file from 'app/entities/file/file.reducer';
import fileBelongings from 'app/entities/file-belongings/file-belongings.reducer';
import version from 'app/entities/version/version.reducer';
import center from 'app/entities/center/center.reducer';
import donor from 'app/entities/donor/donor.reducer';
import centerDonor from 'app/entities/center-donor/center-donor.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  tablet,
  tabletUser,
  panelLog,
  tabletLog,
  tabletUserImage,
  donorImage,
  centerImage,
  tabletUserWatchList,
  tabletWatchList,
  centerWatchList,
  donorWatchList,
  userActivity,
  uploadedFile,
  fileShare,
  file,
  fileBelongings,
  version,
  center,
  donor,
  centerDonor,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
