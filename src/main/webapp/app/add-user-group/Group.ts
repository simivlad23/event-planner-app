import { User } from 'app/add-user-group/User';

export interface Group {
  id: number;
  name: string;
  users: User[];
}

export class GroupObj implements Group {
  id: number;
  name: string;
  users: User[];

  constructor(id: number, name: string, users: User[]) {
    this.id = id;
    this.name = name;
    this.users = users;
  }
}
