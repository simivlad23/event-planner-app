import { Component, OnInit } from '@angular/core';
import { AddUserGroupService } from 'app/add-user-group/add-user-group.service';
import { User } from 'app/add-user-group/User';
import { Group } from 'app/add-user-group/Group';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MessageService } from 'primeng/components/common/api';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-add-user-group',
  templateUrl: './add-user-group.component.html',
  styleUrls: ['./add-user-group.component.scss']
})
export class AddUserGroupComponent implements OnInit {
  groupName: string;
  userLogged: User;
  allUsers: User[] = [];
  selectedUsers: User[] = [];
  responseBackEnd: Observable<HttpResponse<User[]>>;
  checkUniqueGroupName: boolean;

  constructor(private userGroupService: AddUserGroupService, private router: Router, private messageService: MessageService) {}

  ngOnInit() {
    this.userGroupService.getUserLogged().subscribe((resp: User) => {
      this.userLogged = resp;
    });
    this.groupName = '';
    this.getAllUsers();
  }

  getAllUsers() {
    this.responseBackEnd = this.userGroupService.getAllUser();
    this.responseBackEnd.subscribe(resp => {
      this.allUsers = resp.body;
    });
  }

  createGroup() {
    if (this.groupName.length === 0) {
      this.invalidNameGroup();
      return;
    }

    this.userGroupService.checkUniqueGroupName(this.groupName).subscribe(resp => {
      if (resp === 0) {
        this.checkUniqueGroupName = true;
        if (this.validateGroupName() === true) {
          this.selectedUsers.push(this.userLogged);
          const group: Group = { id: 1, name: this.groupName, users: this.selectedUsers };
          this.showConfirm();
          return this.userGroupService.create(group).subscribe(groupDB => {
            this.navigateTo(groupDB.body.id);
          });
        } else {
          this.invalidNameGroup();
          return null;
        }
      }

      this.checkUniqueGroupName = false;
      this.showWarningInvalidName();
    });
  }

  navigateTo(groupId: any) {
    this.router.navigateByUrl('/group-page/' + groupId);
  }

  validateGroupName(): boolean {
    if (this.groupName === undefined) {
      return false;
    }
    if (this.groupName.length < 2) {
      return false;
    }
    return true;
  }

  clear() {
    this.messageService.clear();
  }

  invalidNameGroup() {
    this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Your group name is too short!' });
  }

  showConfirm() {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Group has been created.' });
  }

  showWarningInvalidName() {
    this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Group name already exists. Please change it.' });
  }
}
