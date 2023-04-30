import { IFile } from 'app/shared/model/file.model';
import { IVersion } from 'app/shared/model/version.model';

export interface IFileBelongings {
  id?: number;
  file?: IFile | null;
  version?: IVersion | null;
}

export const defaultValue: Readonly<IFileBelongings> = {};
