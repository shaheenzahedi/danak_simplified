import { ITabletUserImage } from 'app/shared/model/tablet-user-image.model';
import { ITabletUserWatchList } from 'app/shared/model/tablet-user-watch-list.model';
import { IUserActivity } from 'app/shared/model/user-activity.model';
import { ITablet } from 'app/shared/model/tablet.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITabletUser {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  description?: string | null;
  recoveryPhrase?: string | null;
  archived?: boolean | null;
  tabletUserImages?: ITabletUserImage[] | null;
  tabletUserWatchLists?: ITabletUserWatchList[] | null;
  userActivities?: IUserActivity[] | null;
  tablet?: ITablet | null;
  archivedBy?: IUser | null;
  modifiedBy?: IUser | null;
}

export const defaultValue: Readonly<ITabletUser> = {
  archived: false,
};
