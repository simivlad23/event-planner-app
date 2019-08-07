import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { GroupPageComponent } from 'app/group-page/group-page.component';

export const groupPageRoute: Route = {
  path: 'group-page/:id',
  component: GroupPageComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'group-page.page-title'
  },
  canActivate: [UserRouteAccessService]
};
