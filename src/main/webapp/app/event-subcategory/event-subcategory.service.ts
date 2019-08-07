import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEventSubcategory } from 'app/event-subcategory/event-subcategory.model';

@Injectable({
  providedIn: 'root'
})
export class EventSubcategoryService {
  public resourceUrl = SERVER_API_URL + 'api/event-subcategory';

  constructor(protected http: HttpClient) {}

  create(subcategory: IEventSubcategory): Observable<HttpResponse<IEventSubcategory>> {
    return this.http.post<IEventSubcategory>(this.resourceUrl, subcategory, { observe: 'response' });
  }

  findByCategoryId(id: number): Observable<HttpResponse<IEventSubcategory[]>> {
    return this.http.get<IEventSubcategory[]>(`${this.resourceUrl}/get/${id}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<IEventSubcategory>> {
    return this.http.delete<IEventSubcategory>(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}
