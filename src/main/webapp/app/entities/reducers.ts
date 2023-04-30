import tablet from 'app/entities/tablet/tablet.reducer';
import tabletUser from 'app/entities/tablet-user/tablet-user.reducer';
import userActivity from 'app/entities/user-activity/user-activity.reducer';
import file from 'app/entities/file/file.reducer';
import fileBelongings from 'app/entities/file-belongings/file-belongings.reducer';
import version from 'app/entities/version/version.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  tablet,
  tabletUser,
  userActivity,
  file,
  fileBelongings,
  version,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
