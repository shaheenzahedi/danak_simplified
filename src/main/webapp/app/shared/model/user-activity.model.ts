import dayjs from 'dayjs';
import { ITabletUser } from 'app/shared/model/tablet-user.model';

export interface IUserActivity {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  deviceTimeStamp?: string | null;
  listName?: string | null;
  total?: number | null;
  completed?: number | null;
  uniqueName?: string | null;
  activity?: ITabletUser | null;
}

export const defaultValue: Readonly<IUserActivity> = {};
