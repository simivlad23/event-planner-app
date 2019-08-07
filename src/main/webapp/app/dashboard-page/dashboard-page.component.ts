import { Component, OnInit } from '@angular/core';
import { DashboardPageService } from 'app/dashboard-page/dashboard-page.service';
import { Group } from 'app/add-user-group/Group';
import { User } from 'app/add-user-group/User';
import { MessageService } from 'primeng/components/common/api';
import { JoinRequestNotif } from 'app/dashboard-page/JoinRequestNotif';

@Component({
  selector: 'jhi-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit {
  userGroups: Group[];
  userLogged: User;
  userLoggedJoinRequest: JoinRequestNotif[] = [];
  selectedGroup: Group;
  requestMessage: string;
  isPresentUser: boolean;
  totalRecords: number;
  pageNowNo: number;
  pageSize: number;
  allEventsForGroup = new Map();
  collapsedStatusForGroup = new Map();

  constructor(private service: DashboardPageService, private messageService: MessageService) {
    this.totalRecords = 0;
    this.pageNowNo = 0;
    this.pageSize = 5;
    this.userGroups = [];
  }

  getAllEventsForGroup(userGroupID: number) {
    if (this.collapsedStatusForGroup.get(userGroupID)) {
      this.collapsedStatusForGroup.set(userGroupID, false);
      this.service.getAllEventsForGroup(userGroupID).subscribe(async resp => {
        this.allEventsForGroup.set(userGroupID, resp);
      });
      return this.allEventsForGroup;
    } else {
      this.collapsedStatusForGroup.set(userGroupID, true);
    }
  }

  checkCollapseForGroup(userGroupID: number) {
    return this.collapsedStatusForGroup.get(userGroupID);
  }

  isListForGroupEmpty(userGroupID: number) {
    return this.allEventsForGroup.get(userGroupID).length === 0;
  }

  ngOnInit() {
    this.isPresentUser = false;
    this.service.getUserLogged().subscribe((resp: User) => {
      this.userLogged = resp;
      this.service.getUsersJoinRequest(resp.id).subscribe(response => {
        this.userLoggedJoinRequest = response;
      });
    });

    this.mapEventsToGroups(this.pageNowNo, this.pageSize);
  }

  mapEventsToGroups(pageNo: number, pageSize: number) {
    this.service.getAllUserPageGroups(pageNo, pageSize).subscribe(response => {
      this.totalRecords = response['totalElements'];
      this.userGroups = response['content'];

      this.collapsedStatusForGroup = this.userGroups.reduce(function(map, obj) {
        map.set(obj.id, true);
        return map;
      }, new Map());

      this.allEventsForGroup = this.userGroups.reduce(function(map, obj) {
        map.set(obj.id, []);
        return map;
      }, new Map());
    });
  }

  checkUserParticipants(group: Group): boolean {
    for (const user of group.users) {
      if (user.id === this.userLogged.id) {
        return true;
      }
    }
    return false;
  }

  checkUserWaitingList(group: Group): boolean {
    for (const joinRequest of this.userLoggedJoinRequest) {
      if (joinRequest.groupId === group.id) {
        return true;
      }
    }
    return false;
  }

  sentJoinRequest(group: Group) {
    this.selectedGroup = group;
    this.showConfirmRequest(group);
  }

  showConfirmRequest(group: Group) {
    this.messageService.clear();
    this.messageService.add({
      key: 'c',
      sticky: true,
      severity: 'info',
      summary: 'Do you want to join ' + group.name + ' ?',
      detail: 'Leave a message and confirm to proceed.'
    });
  }

  onConfirm() {
    const joinrequest: JoinRequestNotif = {
      requesterId: this.userLogged.id,
      groupId: this.selectedGroup.id,
      joinMessage: this.requestMessage
    };

    this.service.sendJoinrequest(joinrequest).subscribe(request => {
      console.log(request.body);
    });

    this.messageService.clear('c');
    this.messageService.add({ severity: 'success', summary: 'Succes!', detail: 'Request Submitted' });
    this.service.getUsersJoinRequest(this.userLogged.id).subscribe(response => {
      this.userLoggedJoinRequest = response;
    });

    this.service.getUsersJoinRequest(this.userLogged.id).subscribe(response => {
      this.userLoggedJoinRequest = response;
    });

    this.ngOnInit();
  }

  waitingButtonPress() {
    this.messageService.add({ severity: 'error', summary: 'Request', detail: 'Already submitted' });
  }

  onReject() {
    this.messageService.clear('c');
    this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Request canceled' });
  }

  loadGroups(event) {
    let pageNo: number;
    pageNo = 0;
    if (this.totalRecords !== 0) {
      pageNo = event.first / event.rows;
    }
    this.pageNowNo = pageNo;
    this.mapEventsToGroups(this.pageNowNo, this.pageSize);
  }
}
