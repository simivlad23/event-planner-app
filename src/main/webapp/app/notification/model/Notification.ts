import { User } from 'app/add-user-group/User';
import { Moment } from 'moment';

export class Notification {
  id?: number;
  message?: string;
  type?: string;
  valid?: boolean;
  sentBy?: User;
  sentTo?: User;
  userGroup?: any;
  event?: any;
  read?: boolean;
  dateTime?: Moment;

  constructor(
    id?: number,
    message?: string,
    type?: string,
    valid?: boolean,
    sentBy?: User,
    sentTo?: User,
    userGroup?: any,
    event?: any,
    read?: boolean,
    dateTime?: Moment
  ) {
    this.id = id;
    this.message = message;
    this.type = type;
    this.valid = valid;
    this.sentBy = sentBy;
    this.sentTo = sentTo;
    this.event = event;
    this.userGroup = userGroup;
    this.read = read;
    this.dateTime = dateTime;
  }
}
