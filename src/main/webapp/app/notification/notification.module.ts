import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NotificationComponent } from 'app/notification/notification.component';
import { notificationRoute } from 'app/notification/notification.route';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { JhiLanguageService, NgJhipsterModule } from 'ng-jhipster';
import { MessageService, RadioButtonModule, SelectButtonModule, SidebarModule } from 'primeng/primeng';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { JhiLanguageHelper } from 'app/core';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([notificationRoute]),
    ToastModule,
    ButtonModule,
    NgJhipsterModule,
    SidebarModule,
    SelectButtonModule,
    RadioButtonModule,
    FormsModule,
    FontAwesomeModule
  ],
  declarations: [NotificationComponent],
  entryComponents: [NotificationComponent],
  providers: [MessageService, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NotificationModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
