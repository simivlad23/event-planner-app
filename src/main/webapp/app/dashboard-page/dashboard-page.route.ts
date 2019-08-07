import { Route } from '@angular/router';

import { DashboardPageComponent } from 'app/dashboard-page/dashboard-page.component';
import { UserRouteAccessService } from 'app/core';

export const dashboardRoute: Route = {
  path: 'dashboard',
  component: DashboardPageComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'dashboard.title'
  },
  canActivate: [UserRouteAccessService]
};
