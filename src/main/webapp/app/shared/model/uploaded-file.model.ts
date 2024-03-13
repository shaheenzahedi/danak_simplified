import { IFileShare } from 'app/shared/model/file-share.model';

export interface IUploadedFile {
  id?: number;
  createTimeStamp?: string | null;
  deleteTimeStamp?: string | null;
  isPublic?: boolean | null;
  name?: string | null;
  path?: string | null;
  updateTimeStamp?: string | null;
  fileShares?: IFileShare[] | null;
}

export const defaultValue: Readonly<IUploadedFile> = {
  isPublic: false,
};
