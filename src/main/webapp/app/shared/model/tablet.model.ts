import dayjs from 'dayjs';
import { ITabletUser } from 'app/shared/model/tablet-user.model';

export interface ITablet {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  androidId?: string | null;
  macId?: string | null;
  model?: string | null;
  tabletUsers?: ITabletUser[] | null;
}

export const defaultValue: Readonly<ITablet> = {};
