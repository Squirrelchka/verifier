export interface IUserTask {
  id?: number;
  isSolved?: boolean | null;
  username?: string | null;
  taskTitle?: string | null;
}

export const defaultValue: Readonly<IUserTask> = {
  isSolved: false,
};
