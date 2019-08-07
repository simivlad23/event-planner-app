import { Route } from '@angular/router';
import { AddUserGroupComponent } from 'app/add-user-group/add-user-group.component';
import { UserRouteAccessService } from 'app/core';

export const ADD_USER_ROOUTES: Route = {
  path: 'addGroup',
  component: AddUserGroupComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'add-user-group.title'
  },
  canActivate: [UserRouteAccessService]
};
