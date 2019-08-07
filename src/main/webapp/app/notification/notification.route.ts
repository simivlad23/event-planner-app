import { Route } from '@angular/router';

import { NotificationComponent } from 'app/notification/notification.component';
import { UserRouteAccessService } from 'app/core';

export const notificationRoute: Route = {
  path: '',
  component: NotificationComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'notifications.title'
  },
  canActivate: [UserRouteAccessService]
};
