import { IUploadedFile } from 'app/shared/model/uploaded-file.model';
import { ICenter } from 'app/shared/model/center.model';
import { CenterImageType } from 'app/shared/model/enumerations/center-image-type.model';

export interface ICenterImage {
  id?: number;
  centerImageType?: CenterImageType | null;
  file?: IUploadedFile | null;
  center?: ICenter | null;
}

export const defaultValue: Readonly<ICenterImage> = {};
