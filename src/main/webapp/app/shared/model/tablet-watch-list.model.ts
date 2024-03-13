import { ITablet } from 'app/shared/model/tablet.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITabletWatchList {
  id?: number;
  tablet?: ITablet | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITabletWatchList> = {};
