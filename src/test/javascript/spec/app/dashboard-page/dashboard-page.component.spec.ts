import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { DashboardPageComponent } from 'app/dashboard-page/dashboard-page.component';
import { EventPlannerTestModule } from '../../test.module';
import { DashboardPageService } from 'app/dashboard-page/dashboard-page.service';
import { Observable, of } from 'rxjs';
import { MessageService } from 'primeng/api';

describe('Component Tests', () => {
  describe('DashboardPage', () => {
    let fixture: ComponentFixture<DashboardPageComponent>;
    let comp: DashboardPageComponent;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [EventPlannerTestModule],
        declarations: [DashboardPageComponent],
        providers: [MessageService]
      })
        .overrideTemplate(DashboardPageComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(DashboardPageComponent);
      comp = fixture.componentInstance;
    });

    it('should give all userGroups', inject(
      [DashboardPageService],
      fakeAsync((service: DashboardPageService) => {
        tick();
        spyOn(service, 'getAllUserPageGroups').and.returnValues(
          of({
            content: [{ id: 1, name: 'group1' }, { id: 2, name: 'group2' }]
          })
        );
        comp.ngOnInit();
        expect(comp.userGroups.length).toEqual(2);
        expect(comp.userGroups[0].name).toEqual('group1');
      })
    ));

    it('should give all events for one group', inject(
      [DashboardPageService],
      fakeAsync((service: DashboardPageService) => {
        spyOn(service, 'getAllUserPageGroups').and.returnValues(
          of({
            content: [{ id: 1, name: 'group1' }, { id: 2, name: 'group2' }]
          })
        );
        spyOn(service, 'getAllEventsForGroup').and.callFake(function(arg) {
          if (arg === 1) {
            return new Observable(test => test.next([{ id: 1, title: 'event1' }, { id: 2, title: 'event2' }]));
          } else {
            return [];
          }
        });
        comp.ngOnInit();
        tick();
        comp.getAllEventsForGroup(1);
        tick();
        expect(comp.allEventsForGroup.get(1).length).toEqual(2);
      })
    ));
  });
});
