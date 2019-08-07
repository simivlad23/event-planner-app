import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          loadChildren: './admin/admin.module#EventPlannerAdminModule'
        },
        {
          path: 'userGroup',
          loadChildren: './add-user-group/add-user-group.module#AddUserGroupModule'
        },
        {
          path: 'events',
          loadChildren: './event/event.module#EventModule'
        },
        {
          path: 'notifications',
          loadChildren: './notification/notification.module#NotificationModule'
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class EventPlannerAppRoutingModule {}
