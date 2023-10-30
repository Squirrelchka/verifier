import {IUserTask} from 'app/shared/model/user-task.model';

export interface ITask {
  id?: number;
  title?: string | null;
  text?: string | null;
  answer?: string | null;
  userTasks?: IUserTask[] | null;
}

export const defaultValue: Readonly<ITask> = {};
