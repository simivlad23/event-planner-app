import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { Group } from 'app/add-user-group/Group';
import { User } from 'app/add-user-group/User';

type EntityResponseType = HttpResponse<Group>;
type EntityArrayResponseType = HttpResponse<User[]>;

@Injectable({
  providedIn: 'root'
})
export class AddUserGroupService {
  constructor(private http: HttpClient) {}

  getAllUser(): Observable<HttpResponse<[User]>> {
    return this.http.get<[User]>(SERVER_API_URL + '/api/getUsers', { observe: 'response' });
  }

  create(group: Group): Observable<EntityResponseType> {
    return this.http.post<Group>(SERVER_API_URL + '/api/userGroups', group, { observe: 'response' });
  }

  getUserLogged(): Observable<User> {
    return this.http.get<User>(SERVER_API_URL + '/api/account');
  }

  checkUniqueGroupName(groupName: string): Observable<number> {
    return this.http.get<number>(SERVER_API_URL + '/api/userGroups/groupName/' + groupName);
  }
}
