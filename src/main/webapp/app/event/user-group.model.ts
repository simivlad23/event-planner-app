import { IUser } from 'app/core/user/user.model';

export interface IUserGroup {
  id?: number;
  name?: string;
  users?: IUser[];
}

export class UserGroup implements IUserGroup {
  id?: number;
  name?: string;
  users?: IUser[];

  constructor(id?: number, name?: string, users?: IUser[]) {
    this.id = id;
    this.name = name;
    this.users = users;
  }
}
