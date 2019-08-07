import { Routes } from '@angular/router';

import { EventComponent } from 'app/event/event.component';
import { EventAddComponent } from 'app/event/event-add.component';
import { UserRouteAccessService } from 'app/core';

export const EVENT_ROUTES: Routes = [
  {
    path: ':id',
    component: EventComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'event-page.page-title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'add/:id',
    component: EventAddComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'event-add.page-title'
    },
    canActivate: [UserRouteAccessService]
  }
];
