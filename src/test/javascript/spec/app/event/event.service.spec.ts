import { TestBed } from '@angular/core/testing';
import { JhiDateUtils } from 'ng-jhipster';

import { SERVER_API_URL } from 'app/app.constants';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { EventService } from 'app/event';
import { Event } from 'app/event/event.model';
import { HttpResponse } from '@angular/common/http';

describe('Service Tests', () => {
  describe('Event Service Tests', () => {
    let service: EventService;
    let httpMock;
    let expectedResult;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [JhiDateUtils]
      });

      expectedResult = {};
      service = TestBed.get(EventService);
      httpMock = TestBed.get(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    describe('Service methods', () => {
      it('find Event', () => {
        service.find(1).subscribe(received => {
          expectedResult = received.body;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(new Event(1, 'Test Event'));
        expect(expectedResult).toEqual(new Event(1, 'Test Event'));
      });

      it('find all Events', () => {
        service.findAll().subscribe(received => {
          expectedResult = received.body;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([new Event(1, 'Test Event'), new Event(2, 'Other test event')]);
        expect(expectedResult).toEqual([new Event(1, 'Test Event'), new Event(2, 'Other test event')]);
      });

      it('Event not found', () => {
        service.find(2).subscribe(null, (_error: any) => {
          expectedResult = _error.status;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush('Invalid request parameters', {
          status: 404,
          statusText: 'Bad Request'
        });
        expect(expectedResult).toEqual(404);
      });

      it('Event created', () => {
        service
          .create(new Event(1, 'New title'))
          .subscribe((data: HttpResponse<Event>) => expect(data.body).toBe(new Event(1, 'New title')));

        const req = httpMock.expectOne(SERVER_API_URL + 'api/events');
        expect(req.request.method).toBe('POST');
        req.flush(new Event(1, 'New title'));

        httpMock.verify();
      });

      it('Event updated', () => {
        service
          .update(new Event(1, 'New title'))
          .subscribe((data: HttpResponse<Event>) => expect(data.body).toBe(new Event(1, 'New title')));

        const req = httpMock.expectOne(SERVER_API_URL + 'api/events');
        expect(req.request.method).toBe('PUT');
        req.flush(new Event(1, 'New title'));

        httpMock.verify();
      });

      it('Event removed', () => {
        service.delete(1).subscribe((data: any) => expect(data).toBe(1));

        const req = httpMock.expectOne(SERVER_API_URL + 'api/events/1');
        expect(req.request.method).toBe('DELETE');
        req.flush(1);

        httpMock.verify();
      });
    });
  });
});
