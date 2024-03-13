import { IUploadedFile } from 'app/shared/model/uploaded-file.model';
import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { TabletUserImageType } from 'app/shared/model/enumerations/tablet-user-image-type.model';

export interface ITabletUserImage {
  id?: number;
  tabletUserImageType?: TabletUserImageType | null;
  file?: IUploadedFile | null;
  tabletUser?: ITabletUser | null;
}

export const defaultValue: Readonly<ITabletUserImage> = {};
