import dayjs from 'dayjs';
import { ITablet } from 'app/shared/model/tablet.model';

export interface ICenter {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  city?: string | null;
  country?: string | null;
  tablets?: ITablet[] | null;
}

export const defaultValue: Readonly<ICenter> = {};
