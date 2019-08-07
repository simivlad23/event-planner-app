import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { EventPlannerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [EventPlannerSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [EventPlannerSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventPlannerSharedModule {
  static forRoot() {
    return {
      ngModule: EventPlannerSharedModule
    };
  }
}
