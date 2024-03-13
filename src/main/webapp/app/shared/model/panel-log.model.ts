import { IUser } from 'app/shared/model/user.model';
import { PanelLogType } from 'app/shared/model/enumerations/panel-log-type.model';

export interface IPanelLog {
  id?: number;
  createTimeStamp?: string | null;
  panelLogType?: PanelLogType | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPanelLog> = {};
