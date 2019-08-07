import { Moment } from 'moment';
import { IEventCategory } from 'app/event/event-category.model';
import { IUserGroup } from 'app/event/user-group.model';
import { IEventSubcategory } from 'app/event-subcategory';
import { SubEvent } from 'app/event/sub-event.model';

export interface IEvent {
  id?: number;
  title?: string;
  description?: string;
  locationType?: string;
  location?: string;
  dateTime?: Moment;
  eventCategory?: IEventCategory;
  userGroup?: IUserGroup;
  eventSubcategories?: IEventSubcategory[];
  subEvents?: SubEvent[];
}

export class Event implements IEvent {
  id?: number;
  title?: string;
  description?: string;
  locationType?: string;
  location?: string;
  dateTime?: Moment;
  eventCategory?: IEventCategory;
  userGroup?: IUserGroup;
  eventSubcategories?: IEventSubcategory[];
  subEvents?: SubEvent[];

  constructor(
    id?: number,
    title?: string,
    description?: string,
    locationType?: string,
    location?: string,
    dateTime?: Moment,
    eventCategory?: IEventCategory,
    userGroup?: IUserGroup,
    eventSubcategories?: IEventSubcategory[],
    subEvents?: SubEvent[]
  ) {
    this.id = id;
    this.eventCategory = eventCategory;
    this.dateTime = dateTime;
    this.description = description;
    this.location = location;
    this.locationType = locationType;
    this.title = title;
    this.userGroup = userGroup;
    this.eventSubcategories = eventSubcategories;
    this.subEvents = subEvents;
  }
}
