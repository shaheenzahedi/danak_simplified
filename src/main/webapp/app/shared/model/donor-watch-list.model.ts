import { IDonor } from 'app/shared/model/donor.model';
import { IUser } from 'app/shared/model/user.model';

export interface IDonorWatchList {
  id?: number;
  donor?: IDonor | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IDonorWatchList> = {};
