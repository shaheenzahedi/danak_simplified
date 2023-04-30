import { IFileBelongings } from 'app/shared/model/file-belongings.model';
import { IFile } from 'app/shared/model/file.model';

export interface IVersion {
  id?: number;
  version?: number | null;
  fileBelongings?: IFileBelongings[] | null;
  files?: IFile[] | null;
}

export const defaultValue: Readonly<IVersion> = {};
