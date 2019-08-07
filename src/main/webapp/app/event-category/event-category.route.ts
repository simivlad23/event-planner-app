import { Route } from '@angular/router';
import { EventCategoryComponent } from 'app/event-category/event-category.component';
import { UserRouteAccessService } from 'app/core';

export const eventCategoriesRoute: Route = {
  path: 'event-categories',
  component: EventCategoryComponent,
  data: {
    authorities: ['ROLE_ADMIN'],
    pageTitle: 'event-category.page-title'
  },
  canActivate: [UserRouteAccessService]
};
