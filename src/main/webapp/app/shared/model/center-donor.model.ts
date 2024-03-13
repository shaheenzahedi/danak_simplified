import { ICenter } from 'app/shared/model/center.model';
import { IDonor } from 'app/shared/model/donor.model';
import { DonorType } from 'app/shared/model/enumerations/donor-type.model';

export interface ICenterDonor {
  id?: number;
  joinedTimeStamp?: string | null;
  donorType?: DonorType | null;
  center?: ICenter | null;
  donor?: IDonor | null;
}

export const defaultValue: Readonly<ICenterDonor> = {};
