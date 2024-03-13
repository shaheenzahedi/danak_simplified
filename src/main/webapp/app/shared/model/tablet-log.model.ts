import { ITablet } from 'app/shared/model/tablet.model';

export interface ITabletLog {
  id?: number;
  createTimeStamp?: string | null;
  tablet?: ITablet | null;
}

export const defaultValue: Readonly<ITabletLog> = {};
