import { IEventSubcategory } from 'app/event-subcategory/event-subcategory.model';

export interface IEventCategory {
  id?: number;
  name?: string;
  subcategories?: IEventSubcategory[];
}

export class EventCategory implements IEventCategory {
  id?: number;
  name?: string;
  subcategories?: IEventSubcategory[];

  constructor(id?: number, name?: string, subcategories?: IEventSubcategory[]) {
    this.id = id;
    this.name = name;
    this.subcategories = subcategories;
  }
}
