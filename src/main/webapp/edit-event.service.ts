import { Injectable } from '@angular/core';
import { Event } from 'app/event/event.model';

@Injectable({
  providedIn: 'root'
})
export class EditEventService {
  editEvent = false;

  event: Event = null;

  constructor() {}
}
