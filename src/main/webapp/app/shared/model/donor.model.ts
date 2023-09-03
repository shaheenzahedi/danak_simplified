import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ITablet } from 'app/shared/model/tablet.model';

export interface IDonor {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  city?: string | null;
  country?: string | null;
  user?: IUser | null;
  tablets?: ITablet[] | null;
}

export const defaultValue: Readonly<IDonor> = {};
