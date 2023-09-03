import dayjs from 'dayjs';
import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { ICenter } from 'app/shared/model/center.model';
import { IDonor } from 'app/shared/model/donor.model';

export interface ITablet {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  identifier?: string | null;
  model?: string | null;
  tabletUsers?: ITabletUser[] | null;
  center?: ICenter | null;
  donor?: IDonor | null;
}

export const defaultValue: Readonly<ITablet> = {};
