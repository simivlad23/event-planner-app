import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventAddComponent } from 'app/event/event-add.component';
import { Event } from 'app/event/event.model';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateFakeLoader, TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { EventCategory } from 'app/event/event-category.model';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogModule } from 'primeng/dialog';
import { GMapModule } from 'primeng/gmap';
import { CheckboxModule, MessageService } from 'primeng/primeng';
import { ToastModule } from 'primeng/toast';
import { RouterTestingModule } from '@angular/router/testing';

describe('Component Tests', () => {
  describe('Add Event Component Tests', () => {
    let component: EventAddComponent;
    let fixture: ComponentFixture<EventAddComponent>;
    let router: Router;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [
          RouterTestingModule.withRoutes([]),
          ReactiveFormsModule,
          FormsModule,
          DialogModule,
          GMapModule,
          CheckboxModule,
          ToastModule,
          HttpClientTestingModule,
          TranslateModule.forRoot({
            loader: {
              provide: TranslateLoader,
              useClass: TranslateFakeLoader
            }
          })
        ],
        declarations: [EventAddComponent],
        providers: [
          MessageService,
          TranslateService,
          {
            provide: ActivatedRoute,
            Router,
            useValue: {
              data: {
                subscribe: (fn: (value: any) => void) =>
                  fn({
                    id: '1'
                  })
              }
            }
          }
        ]
      });
      router = TestBed.get(Router);

      fixture = TestBed.createComponent(EventAddComponent);
      component = fixture.componentInstance;
      component.editForm = component.fb.group({
        dateTime: [],
        title: new FormControl(component.event.title, [Validators.required]),
        description: new FormControl(component.event.description, [Validators.required]),
        location: new FormControl(component.event.location, [Validators.required]),
        locationType: [],
        eventCategory: new FormControl(component.event.eventCategory, [Validators.required])
      });
      router.initialNavigation();
    });

    it('Invalid form when empty', () => {
      expect(component.editForm.valid).toBeFalsy();
    });

    it('Correct form', () => {
      component.editForm.controls['title'].setValue('Test title');
      component.editForm.controls['description'].setValue('Test description');
      component.editForm.controls['location'].setValue('Test location');
      component.editForm.controls['eventCategory'].setValue(new EventCategory(1, 'Test Category'));

      expect(component.editForm.valid).toBeTruthy();
    });

    let testDateAndTime: any;
    const eventSubcategories = [];
    const subEvents = [];

    it('Test Event creation from form', () => {
      component.editForm.controls['title'].setValue('Test title');
      component.editForm.controls['description'].setValue('Test description');
      component.editForm.controls['location'].setValue('Test location');
      component.editForm.controls['eventCategory'].setValue(new EventCategory(1, 'Test Category'));
      testDateAndTime = moment(new Date(2019, 7, 18), DATE_TIME_FORMAT);
      component.editForm.controls['dateTime'].setValue(testDateAndTime);
    });
  });
});
