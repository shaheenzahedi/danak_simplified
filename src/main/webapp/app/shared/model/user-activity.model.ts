import { ITabletUser } from 'app/shared/model/tablet-user.model';

export interface IUserActivity {
  id?: number;
  listName?: string | null;
  total?: number | null;
  completed?: number | null;
  activity?: ITabletUser | null;
}

export const defaultValue: Readonly<IUserActivity> = {};
