import { Component, OnInit } from '@angular/core';
import { User } from 'app/add-user-group/User';
import { GroupPageService } from 'app/group-page/group-page.service';

@Component({
  selector: 'jhi-add-users-to-group',
  templateUrl: './add-users-to-group.component.html',
  styleUrls: ['./add-users-to-group.component.scss']
})
export class AddUsersToGroupComponent implements OnInit {
  allUsers: User[];
  selectedUsers: User[];
  groupId: number;

  constructor(private groupPageService: GroupPageService) {
    this.allUsers = [];
  }

  ngOnInit() {
    this.groupId = this.groupPageService.groupId;
    this.getAllUsers();
  }

  getAllUsers() {
    this.groupPageService.getUserNotFromGroup(this.groupId).subscribe(resp => {
      this.allUsers = resp.body;
    });
  }

  addUserToGroup() {
    let userIds: { groupId: number; userId: number[] };
    userIds = { groupId: Number(this.groupId), userId: [] };
    this.selectedUsers.forEach(user => {
      userIds.userId.push(user.id);
    });
    this.groupPageService.addUserToGroup(userIds).subscribe(x => {
      this.getAllUsers();
    });
  }
}
