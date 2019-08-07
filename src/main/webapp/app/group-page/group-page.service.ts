import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { User } from 'app/add-user-group/User';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class GroupPageService {
  private _groupId: number; // only to transfer it from a component to another component

  constructor(private http: HttpClient) {}

  get groupId(): number {
    return this._groupId;
  }

  set groupId(value: number) {
    this._groupId = value;
  }

  getAllUser(groupId: number): Observable<HttpResponse<[User]>> {
    return this.http.get<[User]>(SERVER_API_URL + '/api/userGroups/users/' + groupId, { observe: 'response' });
  }

  getGroupById(groupId: number): Observable<HttpResponse<[User]>> {
    return this.http.get<[User]>(SERVER_API_URL + '/api/userGroups/' + groupId, { observe: 'response' });
  }

  addUserToGroup(userIds: any): Observable<HttpResponse<any>> {
    return this.http.put<any>(SERVER_API_URL + '/api/userGroups/users/add/', userIds, { observe: 'response' });
  }

  getUserNotFromGroup(groupId: number) {
    return this.http.get<[User]>(SERVER_API_URL + '/api/userGroups/users/not/' + groupId, { observe: 'response' });
  }

  getEvents(groupId: number): Observable<any> {
    return this.http.get(SERVER_API_URL + '/api/events/usergroups/' + groupId, { observe: 'response' });
  }
}
