import { ICenter } from 'app/shared/model/center.model';
import { IUser } from 'app/shared/model/user.model';

export interface ICenterWatchList {
  id?: number;
  center?: ICenter | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICenterWatchList> = {};
