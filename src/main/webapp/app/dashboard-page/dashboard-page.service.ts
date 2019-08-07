import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Group } from 'app/add-user-group/Group';

import { SERVER_API_URL } from 'app/app.constants';
import { User } from 'app/add-user-group/User';
import { JoinRequestNotif } from 'app/dashboard-page/JoinRequestNotif';

type EntityResponseType = HttpResponse<JoinRequestNotif>;

@Injectable({ providedIn: 'root' })
export class DashboardPageService {
  constructor(private http: HttpClient) {}

  getAllUserGroups(): Observable<Group[]> {
    return this.http.get<[Group]>(SERVER_API_URL + '/api/userGroups');
  }

  getAllEventsForGroup(userGroupID): Observable<any> {
    return this.http.get<any>(SERVER_API_URL + '/api/events/usergroups/' + userGroupID);
  }

  getUserLogged(): Observable<User> {
    return this.http.get<User>(SERVER_API_URL + '/api/account');
  }

  sendJoinrequest(notification: JoinRequestNotif): Observable<EntityResponseType> {
    return this.http.post<JoinRequestNotif>(SERVER_API_URL + '/api/notification/sentJoinRequest', notification, { observe: 'response' });
  }

  getUsersJoinRequest(loggedId: number): Observable<JoinRequestNotif[]> {
    return this.http.get<[JoinRequestNotif]>(SERVER_API_URL + '/api/notifications/getJoinRequest/' + loggedId);
  }

  getAllUserPageGroups(pageNr: number, pageSize: number): Observable<any> {
    return this.http.get<any>(SERVER_API_URL + '/api/userGroups/page?pageNo=' + pageNr + '&pageSize=' + pageSize);
  }
}
