import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EventPlannerSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';
import { DashboardPageComponent } from 'app/dashboard-page/dashboard-page.component';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CounterModule } from 'app/counter/counter.module';
import { DataViewModule } from 'primeng/dataview';
import { PaginatorModule } from 'primeng/primeng';

@NgModule({
  imports: [
    EventPlannerSharedModule,
    InputTextareaModule,
    RouterModule.forChild([HOME_ROUTE]),
    ToastModule,
    ButtonModule,
    CounterModule,
    DataViewModule,
    PaginatorModule
  ],
  declarations: [HomeComponent, DashboardPageComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventPlannerHomeModule {}
