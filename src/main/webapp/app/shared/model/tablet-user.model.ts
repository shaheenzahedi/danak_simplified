import dayjs from 'dayjs';
import { IUserActivity } from 'app/shared/model/user-activity.model';
import { ITablet } from 'app/shared/model/tablet.model';

export interface ITabletUser {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  userActivities?: IUserActivity[] | null;
  tablet?: ITablet | null;
}

export const defaultValue: Readonly<ITabletUser> = {};
