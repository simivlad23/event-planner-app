import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { EventCategoryComponent } from 'app/event-category';
import { EventCategoryService } from 'app/event-category/event-category.service';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import * as HttpStatus from 'http-status-codes';
import { CategorySubcategoryService } from 'app/event-subcategory';

describe('Component Tests', () => {
  describe('EventCategoryComponent', () => {
    let fixture: ComponentFixture<EventCategoryComponent>;
    let comp: EventCategoryComponent;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventCategoryComponent],
        providers: [FormBuilder, CategorySubcategoryService]
      })
        .overrideTemplate(EventCategoryComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(EventCategoryComponent);
      comp = fixture.componentInstance;
      comp.ngOnInit();
    });

    it('should get all data from the service and the status code', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'getAll').and.returnValue(
          of(
            new HttpResponse({
              body: [{ name: 'ceva' }],
              status: HttpStatus.OK
            })
          )
        );
        comp.getAllCategories();
        expect(comp.categories.length).toEqual(1);
        comp.responseBackEnd.toPromise().then(resp => expect(resp.status).toEqual(HttpStatus.OK));
      })
    ));

    it('should set array to void and receive a different status code', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'getAll').and.returnValue(
          of(
            new HttpResponse({
              body: [],
              status: HttpStatus.NO_CONTENT
            })
          )
        );
        comp.getAllCategories();
        expect(comp.categories.length).toEqual(0);
        comp.responseBackEnd.toPromise().then(resp => expect(resp.status).toEqual(HttpStatus.NO_CONTENT));
      })
    ));

    it('should save a category, receiving CREATED and modifying the response to succes', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'save').and.returnValue(
          of(
            new HttpResponse({
              body: [],
              status: HttpStatus.CREATED
            })
          )
        );
        comp.eventCategoryForm.controls['name'].setValue('CategorieValida');
        comp.save();
        expect(comp.response).toEqual('success');
      })
    ));

    it('should save a category that already exists in own list of categories, receiving CONFLICT and modifying the response to fail', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'save').and.returnValue(
          of(
            new HttpResponse({
              body: []
            })
          )
        );
        spyOn(service, 'getAll').and.returnValue(
          of(
            new HttpResponse({
              body: [{ name: 'Categorievalida' }]
            })
          )
        );
        comp.getAllCategories();
        tick();
        comp.eventCategoryForm.controls['name'].setValue('Categorievalida');
        comp.save();
        expect(comp.response).toEqual('fail');
      })
    ));

    it('should save a category that already exists, receiving CONFLICT and modifying the response to fail', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'save').and.returnValue(
          of(
            new HttpResponse({
              body: [],
              status: HttpStatus.CONFLICT
            })
          )
        );
        comp.eventCategoryForm.controls['name'].setValue('CategorieValida');
        comp.save();
        expect(comp.response).toEqual('fail');
      })
    ));

    it('should save a category that is null, receiving BAD_REQUEST and modifying the response to null', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'save').and.returnValue(
          of(
            new HttpResponse({
              body: [],
              status: HttpStatus.BAD_REQUEST
            })
          )
        );
        comp.eventCategoryForm.controls['name'].setValue('CategorieValida');
        comp.save();
        expect(comp.response).toEqual('null');
      })
    ));

    it('should save a category that is null, receiving AnyOther and modifying the response to error', inject(
      [EventCategoryService],
      fakeAsync((service: EventCategoryService) => {
        tick();
        spyOn(service, 'save').and.returnValue(
          of(
            new HttpResponse({
              body: [],
              status: HttpStatus.IM_A_TEAPOT
            })
          )
        );
        comp.eventCategoryForm.controls['name'].setValue('CategorieValida');
        comp.save();
        expect(comp.response).toEqual('error');
      })
    ));

    it('should capitalize the first letter and the others make lower case', inject(
      [EventCategoryService],
      fakeAsync(() => {
        tick();
        expect(comp.capitalizeFirstLetter('something')).toEqual('Something');
        expect(comp.capitalizeFirstLetter('somethiNg')).toEqual('Something');
        expect(comp.capitalizeFirstLetter('SOMETHING')).toEqual('Something');
      })
    ));
  });
});
