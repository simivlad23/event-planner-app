import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { User } from 'app/add-user-group/User';

@Injectable({
  providedIn: 'root'
})
export class CounterService {
  constructor(private http: HttpClient) {}

  getMyVote(user_id: number, event_id: number): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/my-vote/' + user_id + '/' + event_id, {
      observe: 'response',
      responseType: 'text'
    });
  }

  getYesNoMaybeCounter(eventId: any): Observable<HttpResponse<any>> {
    return this.http.get<any>(SERVER_API_URL + 'api/votes/' + eventId, { observe: 'response' });
  }

  save(vote: any): Observable<HttpResponse<any>> {
    return this.http.post(SERVER_API_URL + 'api/votes', vote, { observe: 'response' });
  }

  getLoggedUser(): Observable<User> {
    return this.http.get<User>(SERVER_API_URL + '/api/account');
  }
}
