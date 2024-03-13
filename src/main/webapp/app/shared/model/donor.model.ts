import { ICenterDonor } from 'app/shared/model/center-donor.model';
import { IDonorImage } from 'app/shared/model/donor-image.model';
import { IDonorWatchList } from 'app/shared/model/donor-watch-list.model';
import { ITablet } from 'app/shared/model/tablet.model';
import { IUser } from 'app/shared/model/user.model';
import { EducationType } from 'app/shared/model/enumerations/education-type.model';

export interface IDonor {
  id?: number;
  createTimeStamp?: string | null;
  updateTimeStamp?: string | null;
  name?: string | null;
  city?: string | null;
  country?: string | null;
  nationalCode?: string | null;
  educationType?: EducationType | null;
  education?: string | null;
  occupation?: string | null;
  workPlace?: string | null;
  workPlacePhone?: string | null;
  archived?: boolean | null;
  otpPhoneCode?: number | null;
  otpPhoneEnable?: boolean | null;
  otpPhoneSentTimeStamp?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  uid?: string | null;
  centerDonors?: ICenterDonor[] | null;
  donorImages?: IDonorImage[] | null;
  donorWatchLists?: IDonorWatchList[] | null;
  tablets?: ITablet[] | null;
  user?: IUser | null;
  archivedBy?: IUser | null;
  createdBy?: IUser | null;
  modifiedBy?: IUser | null;
}

export const defaultValue: Readonly<IDonor> = {
  archived: false,
  otpPhoneEnable: false,
};
