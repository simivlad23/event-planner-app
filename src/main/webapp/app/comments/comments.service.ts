import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IComment } from 'app/comments/comment.model';
import { User } from 'app/add-user-group/User';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {
  public resourceUrl = SERVER_API_URL + 'api/comments';

  constructor(protected http: HttpClient) {}

  create(comment: IComment): Observable<HttpResponse<IComment>> {
    return this.http.post<IComment>(this.resourceUrl, comment, { observe: 'response' });
  }

  findAllByEventId(id: number): Observable<HttpResponse<IComment[]>> {
    return this.http.get<IComment[]>(`${this.resourceUrl}/event/${id}`, { observe: 'response' });
  }

  delete(id: number) {
    return this.http.delete<IComment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserLogged(): Observable<User> {
    return this.http.get<User>(SERVER_API_URL + '/api/account');
  }
}
