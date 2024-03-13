import { IUploadedFile } from 'app/shared/model/uploaded-file.model';
import { IUser } from 'app/shared/model/user.model';

export interface IFileShare {
  id?: number;
  file?: IUploadedFile | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IFileShare> = {};
