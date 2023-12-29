import { ICenter } from 'app/shared/model/center.model';
import { IDonor } from 'app/shared/model/donor.model';

export interface ICenterDonor {
  id?: number;
  center?: ICenter | null;
  donor?: IDonor | null;
}

export const defaultValue: Readonly<ICenterDonor> = {};
