import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { EventPlannerSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { GroupPageComponent } from 'app/group-page/group-page.component';
import { groupPageRoute } from 'app/group-page/group-page.route';
import { ListboxModule } from 'primeng/listbox';
import { SidebarModule } from 'primeng/primeng';
import { MatListModule, MatSidenavModule } from '@angular/material';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [
    EventPlannerSharedModule,
    RouterModule.forChild([groupPageRoute]),
    ListboxModule,
    SidebarModule,
    MatListModule,
    FullCalendarModule,
    MatSidenavModule
  ],
  providers: [],
  declarations: [GroupPageComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GroupPageModule {}
