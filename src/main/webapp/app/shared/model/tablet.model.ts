import { ITabletLog } from 'app/shared/model/tablet-log.model';
import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { ITabletWatchList } from 'app/shared/model/tablet-watch-list.model';
import { ICenter } from 'app/shared/model/center.model';
import { IDonor } from 'app/shared/model/donor.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITablet {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  identifier?: string | null;
  tag?: string | null;
  name?: string | null;
  androidId?: string | null;
  macId?: string | null;
  model?: string | null;
  description?: string | null;
  archived?: boolean | null;
  tabletLogs?: ITabletLog[] | null;
  tabletUsers?: ITabletUser[] | null;
  tabletWatchLists?: ITabletWatchList[] | null;
  center?: ICenter | null;
  donor?: IDonor | null;
  archivedBy?: IUser | null;
  modifiedBy?: IUser | null;
}

export const defaultValue: Readonly<ITablet> = {
  archived: false,
};
