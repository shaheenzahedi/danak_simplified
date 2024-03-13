import { IUploadedFile } from 'app/shared/model/uploaded-file.model';
import { IDonor } from 'app/shared/model/donor.model';
import { DonorImageType } from 'app/shared/model/enumerations/donor-image-type.model';

export interface IDonorImage {
  id?: number;
  donorImageType?: DonorImageType | null;
  file?: IUploadedFile | null;
  donor?: IDonor | null;
}

export const defaultValue: Readonly<IDonorImage> = {};
