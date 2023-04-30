import { IFileBelongings } from 'app/shared/model/file-belongings.model';
import { IVersion } from 'app/shared/model/version.model';

export interface IFile {
  id?: number;
  name?: string | null;
  checksum?: string | null;
  path?: string | null;
  fileBelongings?: IFileBelongings[] | null;
  placement?: IVersion | null;
}

export const defaultValue: Readonly<IFile> = {};
