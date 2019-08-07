import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { EventPlannerSharedModule } from 'app/shared';
import { EventPlannerCoreModule } from 'app/core';
import { EventPlannerAppRoutingModule } from './app-routing.module';
import { EventPlannerHomeModule } from './home/home.module';
import { EventPlannerAccountModule } from './account/account.module';
import { EventPlannerEntityModule } from './entities/entity.module';
import { NotificationModule } from 'app/notification/notification.module';
import * as moment from 'moment';
import { AddUserGroupModule } from 'app/add-user-group/add-user-group.module';
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';
import { EventModule } from 'app/event/event.module';
import { EventCategoryModule } from 'app/event-category';
import { EventSubcategoryAddComponent } from './event-subcategory/event-subcategory-add.component';
import { CategorySubcategoryService } from 'app/event-subcategory';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MultiSelectModule } from 'primeng/multiselect';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { CounterComponent } from 'app/counter/counter.component';
import { GroupPageModule } from 'app/group-page/group-page.module';
import { AddUsersToGroupComponent } from './group-page/add-users-to-group/add-users-to-group.component';
import { ListboxModule } from 'primeng/listbox';
import { CommentsComponent } from './comments/comments.component';
import { CommentsModule } from 'app/comments/comments.module';

// jhipster-needle-angular-add-module-import JHipster will add new module here

@NgModule({
  imports: [
    BrowserModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-' }),
    NgJhipsterModule.forRoot({
      // set below to true to make alerts look like toast
      alertAsToast: false,
      alertTimeout: 5000,
      i18nEnabled: true,
      defaultI18nLang: 'en'
    }),
    EventPlannerSharedModule.forRoot(),
    EventPlannerCoreModule,
    EventPlannerHomeModule,
    EventPlannerAccountModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    GroupPageModule,
    EventCategoryModule,
    EventPlannerEntityModule,
    EventPlannerAppRoutingModule,
    EventPlannerAppRoutingModule,
    AddUserGroupModule,
    EventModule,
    NotificationModule,
    EventModule,
    BrowserAnimationsModule,
    MultiSelectModule,
    MessagesModule,
    ToastModule,
    ButtonModule,
    ListboxModule,
    CommentsModule
  ],
  entryComponents: [EventSubcategoryAddComponent, AddUsersToGroupComponent],
  declarations: [
    JhiMainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    EventSubcategoryAddComponent,
    AddUsersToGroupComponent
  ],
  providers: [
    CategorySubcategoryService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationInterceptor,
      multi: true
    },
    CounterComponent,
    CommentsComponent
  ],
  exports: [],
  bootstrap: [JhiMainComponent]
})
export class EventPlannerAppModule {
  constructor(private dpConfig: NgbDatepickerConfig) {
    this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
  }
}
