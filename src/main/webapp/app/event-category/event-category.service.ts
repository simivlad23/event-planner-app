import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { EventCategory } from 'app/event-category/event-category.component';

@Injectable({
  providedIn: 'root'
})
export class EventCategoryService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<HttpResponse<[EventCategory]>> {
    return this.http.get<[EventCategory]>(SERVER_API_URL + 'api/event-categories', { observe: 'response' });
  }

  save(eventCategory: any): Observable<any> {
    // const config  = { headers: new HttpHeaders().set('Content-Type', 'application/json') };
    return this.http.post(SERVER_API_URL + 'api/event-categories', eventCategory, { observe: 'response' });
  }
}
