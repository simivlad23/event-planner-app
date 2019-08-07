export interface ISubEvent {
  id?: number;
  details?: string;
  title?: string;
  lat?: number;
  lng?: number;
}

export class SubEvent implements ISubEvent {
  id?: number;
  title?: string;
  details?: string;
  lat?: number;
  lng?: number;

  constructor(id?: number, title?: string, details?: string, lat?: number, lng?: number) {
    this.id = id;
    this.title = title;
    this.details = details;
    this.lat = lat;
    this.lng = lng;
  }
}
