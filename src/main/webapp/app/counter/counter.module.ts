import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { EventPlannerSharedModule } from 'app/shared';
import { CounterComponent } from 'app/counter/counter.component';

@NgModule({
  imports: [EventPlannerSharedModule],
  declarations: [CounterComponent],
  exports: [CounterComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CounterModule {}
