import { IEventCategory } from 'app/event/event-category.model';

export interface IEventSubcategory {
  id?: number;
  name?: string;
  eventCategory?: IEventCategory;
}

export class EventSubcategory implements IEventSubcategory {
  id?: number;
  name?: string;
  eventCategory?: IEventCategory;

  public EventSubcategory(id?: number, name?: string, eventCategory?: IEventCategory) {
    this.id = id;
    this.name = name;
    this.eventCategory = eventCategory;
  }
}
