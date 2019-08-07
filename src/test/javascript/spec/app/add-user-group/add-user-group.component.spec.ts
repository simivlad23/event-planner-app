import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { AddUserGroupComponent } from 'app/add-user-group/add-user-group.component';
import { AddUserGroupService } from 'app/add-user-group/add-user-group.service';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

describe('Component Tests', () => {
  describe('AddUserGroupComponent', () => {
    let fixture: ComponentFixture<AddUserGroupComponent>;
    let comp: AddUserGroupComponent;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
        declarations: [AddUserGroupComponent],
        providers: [
          FormBuilder,
          MessageService,
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
      })
        .overrideTemplate(AddUserGroupComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(AddUserGroupComponent);
      comp = fixture.componentInstance;
      comp.ngOnInit();
    });

    it('should get all users from the service and the status code', inject(
      [AddUserGroupService],
      fakeAsync((service: AddUserGroupService) => {
        tick();
        spyOn(service, 'getAllUser').and.returnValue(
          of(
            new HttpResponse({
              body: [{ name: 'ceva' }]
            })
          )
        );
        comp.getAllUsers();
        tick();
        expect(comp.allUsers.length).toEqual(1);
      })
    ));

    it('should verify the group name: if exists or not', inject(
      [],
      fakeAsync(() => {
        tick();
        comp.groupName = '';
        expect(comp.validateGroupName()).toEqual(false);
        comp.groupName = 'group2';
        expect(comp.validateGroupName()).toEqual(true);
      })
    ));
  });
});
