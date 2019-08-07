import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AddUserGroupComponent } from 'app/add-user-group/add-user-group.component';
import { ADD_USER_ROOUTES } from 'app/add-user-group/add-user-group.route';
import { EventPlannerSharedModule } from 'app/shared';
import { ListboxModule } from 'primeng/listbox';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

@NgModule({
  imports: [
    EventPlannerSharedModule,
    ListboxModule,
    CommonModule,
    FormsModule,
    MultiSelectModule,
    MessagesModule,
    ToastModule,
    ButtonModule,
    RouterModule.forChild([ADD_USER_ROOUTES])
  ],
  providers: [MessageService, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  declarations: [AddUserGroupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AddUserGroupModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
