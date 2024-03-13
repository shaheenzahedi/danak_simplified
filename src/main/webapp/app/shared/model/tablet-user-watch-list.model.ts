import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITabletUserWatchList {
  id?: number;
  tabletUser?: ITabletUser | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITabletUserWatchList> = {};
