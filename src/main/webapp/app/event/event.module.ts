import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventComponent } from './event.component';
import { JhiLanguageService, NgJhipsterModule } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { RouterModule } from '@angular/router';
import { EVENT_ROUTES } from 'app/event/event.route';
import { EventAddComponent } from 'app/event/event-add.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CounterModule } from 'app/counter/counter.module';
import { MatCheckboxModule, MatDividerModule, MatExpansionModule, MatSidenavModule } from '@angular/material';
import { ToastModule } from 'primeng/toast';
import { GMapModule } from 'primeng/gmap';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule, CheckboxModule, MessageService } from 'primeng/primeng';
import { DropdownModule } from 'primeng/dropdown';
import { CommentsModule } from 'app/comments/comments.module';

@NgModule({
  declarations: [EventComponent, EventAddComponent],
  entryComponents: [EventComponent, EventAddComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(EVENT_ROUTES),
    ReactiveFormsModule,
    NgJhipsterModule,
    CounterModule,
    MatExpansionModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatDividerModule,
    ToastModule,
    GMapModule,
    DialogModule,
    CheckboxModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    MatDividerModule,
    CommentsModule
  ],
  providers: [MessageService, { provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
