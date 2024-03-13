import { ICenterDonor } from 'app/shared/model/center-donor.model';
import { ICenterImage } from 'app/shared/model/center-image.model';
import { ICenterWatchList } from 'app/shared/model/center-watch-list.model';
import { ITablet } from 'app/shared/model/tablet.model';
import { IUser } from 'app/shared/model/user.model';
import { CenterType } from 'app/shared/model/enumerations/center-type.model';

export interface ICenter {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  city?: string | null;
  country?: string | null;
  archived?: boolean | null;
  centerType?: CenterType | null;
  latitude?: number | null;
  longitude?: number | null;
  centerDonors?: ICenterDonor[] | null;
  centerImages?: ICenterImage[] | null;
  centerWatchLists?: ICenterWatchList[] | null;
  tablets?: ITablet[] | null;
  archivedBy?: IUser | null;
  createdBy?: IUser | null;
  modifiedBy?: IUser | null;
}

export const defaultValue: Readonly<ICenter> = {
  archived: false,
};
