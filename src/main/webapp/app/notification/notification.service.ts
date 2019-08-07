import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { User } from 'app/add-user-group/User';
import { Notification } from 'app/notification/model/Notification';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private http: HttpClient) {}

  getAllNotificationsForUser(id: number): Observable<HttpResponse<Notification[]>> {
    return this.http.get<Notification[]>(SERVER_API_URL + '/api/notifications/' + id, { observe: 'response' });
  }

  getLoggedUser(): Observable<User> {
    return this.http.get<User>(SERVER_API_URL + '/api/account');
  }

  acceptGroupRequest(notificationId: number): Observable<HttpResponse<any>> {
    return this.http.put<any>(SERVER_API_URL + '/api/notifications/acceptRequest/' + notificationId, null, { observe: 'response' });
  }

  rejectGroupRequest(notificationId: number): Observable<HttpResponse<any>> {
    return this.http.put<any>(SERVER_API_URL + '/api/notifications/rejectRequest/' + notificationId, null, { observe: 'response' });
  }

  sendJoinResponse(notification: Notification) {
    return this.http.post<Notification>(SERVER_API_URL + '/api/notifications/sendJoinResponse', notification);
  }

  markNotificationAsRead(notificationId: number) {
    return this.http.put<any>(SERVER_API_URL + '/api/notifications/markAsRead/' + notificationId, null, { observe: 'response' });
  }

  getAllReadNotificationsForUser(userId: number): Observable<HttpResponse<Notification[]>> {
    return this.http.get<Notification[]>(SERVER_API_URL + '/api/notifications/read/' + userId, { observe: 'response' });
  }

  getAllUnreadNotificationsForUser(userId: number): Observable<HttpResponse<Notification[]>> {
    return this.http.get<Notification[]>(SERVER_API_URL + '/api/notifications/unread/' + userId, { observe: 'response' });
  }
}
