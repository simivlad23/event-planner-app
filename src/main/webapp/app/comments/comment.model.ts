import { User } from 'app/add-user-group/User';
import { Event } from 'app/event/event.model';
import { Moment } from 'moment';

export interface IComment {
  id?: number;
  text?: string;
  dateTime?: Moment;
  user?: User;
  event?: Event;
}

export class Comment implements IComment {
  id?: number;
  text?: string;
  dateTime?: Moment;
  user?: User;
  event?: Event;

  public Comment(id?: number, text?: string, dateTime?: Moment, user?: User, event?: Event) {
    this.id = id;
    this.text = text;
    this.dateTime = dateTime;
    this.user = user;
    this.event = event;
  }
}
