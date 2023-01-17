import { ITabletUser } from 'app/shared/model/tablet-user.model';

export interface ITablet {
  id?: number;
  name?: string | null;
  tabletUsers?: ITabletUser[] | null;
}

export const defaultValue: Readonly<ITablet> = {};
