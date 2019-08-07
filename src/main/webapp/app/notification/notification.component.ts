import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'app/notification/notification.service';
import { User } from 'app/add-user-group/User';
import { Notification } from 'app/notification/model/Notification';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MessageService } from 'primeng/components/common/api';
import * as HttpStatus from 'http-status-codes';

@Component({
  selector: 'jhi-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
  userNotifications: Notification[];
  loggedUser: User;
  modifiedNotification: Boolean;
  selectedNotification: Notification;
  wasAccepted: Boolean;
  showSidebar: Boolean;
  selectedRadioButton: string;

  constructor(private notificationService: NotificationService, private modalService: NgbModal, private messageService: MessageService) {}

  ngOnInit() {
    this.notificationService.getLoggedUser().subscribe((response: User) => {
      this.loggedUser = response;
      this.notificationService.getAllNotificationsForUser(this.loggedUser.id).subscribe(resp => (this.userNotifications = resp.body));
    });
  }

  markNotificationAsRead() {
    if (this.selectedNotification.read === false) {
      this.notificationService.markNotificationAsRead(this.selectedNotification.id).subscribe(response => {
        switch (this.selectedRadioButton) {
          case 'Unread': {
            this.notificationService
              .getAllUnreadNotificationsForUser(this.loggedUser.id)
              .subscribe(resp => (this.userNotifications = resp.body));
            break;
          }
          case 'All': {
            this.notificationService.getAllNotificationsForUser(this.loggedUser.id).subscribe(resp => (this.userNotifications = resp.body));
            break;
          }
          default: {
            this.notificationService.getAllNotificationsForUser(this.loggedUser.id).subscribe(resp => (this.userNotifications = resp.body));
            break;
          }
        }
      });
    }
  }

  showJoinRequest(notification: Notification) {
    this.selectedNotification = notification;
    this.messageService.clear();
    this.messageService.add({
      key: 'joinGroupRequest',
      sticky: true,
      severity: 'info',
      detail: 'Hi there! I want to join group ' + '"' + this.selectedNotification.userGroup.name + '"'
    });
    this.markNotificationAsRead();
  }

  showReminder(notification: Notification) {
    this.selectedNotification = notification;
    this.messageService.clear();
    this.messageService.add({
      key: 'reminder',
      sticky: true,
      severity: 'info',
      detail: this.selectedNotification.message
    });
    this.markNotificationAsRead();
  }

  showInfo(notification: Notification) {
    this.selectedNotification = notification;
    this.messageService.clear();
    this.messageService.add({
      key: 'info',
      sticky: true,
      severity: 'info',
      detail: this.selectedNotification.message
    });
    this.markNotificationAsRead();
  }

  joinResponse(wasAccepted: Boolean): Notification {
    let joinMessage: string;
    if (wasAccepted === true) {
      joinMessage = 'You were accepted in group ' + '"' + this.selectedNotification.userGroup.name + '"';
    } else {
      joinMessage = 'You were not accepted in group ' + '"' + this.selectedNotification.userGroup.name + '"';
    }

    const response: Notification = {
      message: joinMessage,
      valid: true,
      read: false,
      type: 'INFO',
      sentBy: this.loggedUser,
      sentTo: this.selectedNotification.sentBy
    };
    return response;
  }

  acceptGroupRequest() {
    this.notificationService.acceptGroupRequest(this.selectedNotification.id).subscribe(resp => {
      switch (resp.status) {
        case HttpStatus.OK: {
          this.modifiedNotification = true;
          this.onAcceptJoinRequestAlert(this.modifiedNotification);
          this.wasAccepted = true;
          this.notificationService.sendJoinResponse(this.joinResponse(this.wasAccepted)).subscribe();
          break;
        }
        case HttpStatus.NO_CONTENT: {
          this.modifiedNotification = false;
          this.onAcceptJoinRequestAlert(this.modifiedNotification);
          break;
        }
      }
    });
  }

  rejectGroupRequest() {
    this.notificationService.rejectGroupRequest(this.selectedNotification.id).subscribe(resp => {
      switch (resp.status) {
        case HttpStatus.OK: {
          this.modifiedNotification = true;
          this.onRejectJoinRequestAlert(this.modifiedNotification);
          this.wasAccepted = false;
          this.notificationService.sendJoinResponse(this.joinResponse(this.wasAccepted)).subscribe();
          break;
        }
        case HttpStatus.NO_CONTENT: {
          this.modifiedNotification = false;
          this.onRejectJoinRequestAlert(this.modifiedNotification);
          break;
        }
      }
    });
  }

  onAcceptJoinRequestAlert(bool: Boolean) {
    if (bool === true) {
      this.messageService.clear('joinGroupRequest');
      this.messageService.add({ severity: 'success', summary: 'Succes!', detail: 'Request Submitted' });
      return;
    }
    this.messageService.clear('joinGroupRequest');
    this.messageService.add({ severity: 'warn', summary: 'Warning!', detail: 'Request was already submitted' });
  }

  onRejectJoinRequestAlert(bool: Boolean) {
    if (bool === true) {
      this.messageService.clear('joinGroupRequest');
      this.messageService.add({ severity: 'success', summary: 'Succes!', detail: 'Request Submitted' });
      return;
    }
    this.messageService.clear('joinGroupRequest');
    this.messageService.add({ severity: 'warn', summary: 'Warning!', detail: 'Request was already submitted' });
  }

  viewSideBar() {
    this.showSidebar = !this.showSidebar;
  }

  showAllReadNotificationsForUser() {
    this.notificationService
      .getAllReadNotificationsForUser(this.loggedUser.id)
      .subscribe(response => (this.userNotifications = response.body));
  }

  showAllUnreadNotificationsForUser() {
    this.notificationService
      .getAllUnreadNotificationsForUser(this.loggedUser.id)
      .subscribe(response => (this.userNotifications = response.body));
  }

  showAllNotificationsForUser() {
    this.notificationService.getAllNotificationsForUser(this.loggedUser.id).subscribe(response => (this.userNotifications = response.body));
  }
}
