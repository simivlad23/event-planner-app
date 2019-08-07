import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { EventService } from 'app/event/event.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { EventCategory, IEventCategory } from 'app/event/event-category.model';
import { UserGroup } from 'app/event/user-group.model';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared';
import { Event, IEvent } from 'app/event/event.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventCategoryService } from 'app/event-category/event-category.service';
import * as HttpStatus from 'http-status-codes';
import { ActivatedRoute, Router } from '@angular/router';
import { EventSubcategory, EventSubcategoryService } from 'app/event-subcategory';
import { MessageService } from 'primeng/api';
import { SubEvent } from 'app/event/sub-event.model';
import { EditEventService } from '../../edit-event.service';

declare var google: any;

@Component({
  selector: 'jhi-event-add',
  templateUrl: './event-add.component.html',
  styleUrls: ['./event-add.component.scss']
})
export class EventAddComponent implements OnInit {
  successful: boolean;

  isSaving: boolean;

  userGroup: UserGroup;

  eventCategories: EventCategory[] = [];

  eventSubcategories: EventSubcategory[] = [];

  result: Observable<HttpResponse<IEvent>>;

  event: Event = new Event();

  editEvent: Event = null;

  editForm: FormGroup;

  responseBackEnd: Observable<HttpResponse<any>>;

  chosenCategory: EventCategory = null;

  activeSubcategories: boolean[] = [];

  listSub = [];

  subEvents: SubEvent[] = [];

  editSubEvents: SubEvent[] = [];

  options: any;

  overlays: any[] = [];

  editOverlays: any[] = [];

  dialogVisible: boolean;

  markerTitle: string;

  markerDescription: string;

  selectedPosition: any;

  infoWindow: any;

  draggable: boolean;

  date: string;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected eventService: EventService,
    protected subcategoryService: EventSubcategoryService,
    public fb: FormBuilder,
    protected eventCategoryService: EventCategoryService,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private editService: EditEventService
  ) {
    this.isSaving = false;
  }

  ngOnInit() {
    this.getAllCategories();

    this.route.params.subscribe(params => (this.userGroup = new UserGroup(params['id'])));

    this.editForm = this.fb.group({
      dateTime: [],
      title: new FormControl(this.event.title, [Validators.required]),
      description: new FormControl(this.event.description, [Validators.required]),
      location: new FormControl(this.event.location, [Validators.required]),
      locationType: [],
      eventCategory: new FormControl(this.event.eventCategory, [Validators.required])
    });

    this.editForm.valueChanges.subscribe(() => {
      this.successful = false;
      if (this.chosenCategory !== this.editForm.get(['eventCategory']).value) {
        this.chosenCategory = this.editForm.get(['eventCategory']).value;
        if (this.chosenCategory !== null) {
          this.getSubCategories(this.chosenCategory.id);
        } else {
          this.eventSubcategories = [];
        }
      }
    });

    this.options = {
      center: { lat: 46.78752375382936, lng: 23.59417819773182 },
      zoom: 17
    };

    this.infoWindow = new google.maps.InfoWindow();

    if (this.editService.editEvent === true) {
      this.editEvent = this.editService.event;
      this.editForm.controls['title'].setValue(this.editEvent.title);
      this.editForm.controls['description'].setValue(this.editEvent.description);
      this.editForm.controls['location'].setValue(this.editEvent.location);
      this.editForm.controls['eventCategory'].setValue(this.editEvent.eventCategory);
      this.getSubCategories(this.editEvent.eventCategory.id);
      this.initOverlays();
      this.date = this.editEvent.dateTime.toString().slice(0, 16);
      this.editService.editEvent = false;
    }
  }

  getAllCategories() {
    this.responseBackEnd = this.eventCategoryService.getAll();
    this.responseBackEnd.subscribe(resp => {
      if (resp.status === HttpStatus.OK) {
        this.eventCategories = resp.body;
      }
      if (resp.status === HttpStatus.CONFLICT) {
        this.eventCategories = null;
      }
    });
  }

  getSubCategories(id: number) {
    this.subcategoryService.findByCategoryId(id).subscribe((res: HttpResponse<EventSubcategory[]>) => {
      if (res.status === HttpStatus.OK) {
        this.eventSubcategories = res.body;
        this.activeSubcategories = [];
        for (const index in this.eventSubcategories) {
          if (this.eventSubcategories.hasOwnProperty(index)) {
            this.activeSubcategories.push(false);
          }
        }
        if (this.editEvent !== null) {
          for (const sub of this.editEvent.eventSubcategories) {
            for (const index in this.eventSubcategories) {
              if (sub.name === this.eventSubcategories[index].name) {
                this.activeSubcategories[index] = true;
              }
            }
          }
        }
      }
      if (res.status === HttpStatus.NO_CONTENT) {
        this.eventSubcategories = [];
      }
    });
  }

  activateSubcategory(index: number) {
    this.activeSubcategories[index] = !this.activeSubcategories[index];
  }

  getActiveSubcategories(): EventSubcategory[] {
    this.listSub = [];
    for (const index in this.activeSubcategories) {
      if (this.activeSubcategories[index] === true) {
        this.listSub.push(this.eventSubcategories[index]);
      }
    }
    return this.listSub;
  }

  save() {
    this.isSaving = true;
    this.event = this.createFromForm();
    if (this.editEvent !== null) {
      this.event.id = this.editEvent.id;
      this.result = this.eventService.update(this.event);
    } else {
      this.result = this.eventService.create(this.event);
    }
    this.result.subscribe(
      result => {
        this.successful = true;
        if (this.editEvent === null) {
          this.navigateTo(result.body.id);
        } else {
          this.navigateTo(this.editEvent.id);
        }
      },
      () => (this.successful = false)
    );
    this.isSaving = false;
  }

  navigateTo(eventId: any) {
    // todo: cate pagina evenimentului

    this.router.navigateByUrl('/events/' + eventId);
  }

  trackEventCategoryById(item: IEventCategory) {
    return item.id;
  }

  createFromForm(): IEvent {
    this.mapperMarkersToSubEvents();
    console.log(this.subEvents);
    return {
      id: null,
      dateTime: moment(this.date, DATE_TIME_FORMAT),
      title: this.editForm.get(['title']).value,
      description: this.editForm.get(['description']).value,
      locationType: 'ADDRESS',
      location: this.editForm.get(['location']).value,
      eventCategory: this.editForm.get(['eventCategory']).value,
      userGroup: this.userGroup,
      eventSubcategories: this.getActiveSubcategories(),
      subEvents: this.subEvents
    };
  }

  handleMapClick(event) {
    this.dialogVisible = true;
    this.selectedPosition = event.latLng;
  }

  handleOverlayClick(event) {
    const isMarker = event.overlay.getTitle !== undefined;
    console.log(this.overlays);
    console.log(event.overlay);
    if (isMarker) {
      const title = event.overlay.getTitle();
      const details = event.overlay['details'];
      this.infoWindow.setContent('' + title + ' ' + details);
      this.infoWindow.open(event.map, event.overlay);
      event.map.setCenter(event.overlay.getPosition());

      this.messageService.add({ severity: 'info', summary: 'Marker Selected', detail: title });
    } else {
      this.messageService.add({ severity: 'info', summary: 'Shape Selected', detail: '' });
    }
  }

  addMarker() {
    this.overlays.push(
      new google.maps.Marker({
        position: { lat: this.selectedPosition.lat(), lng: this.selectedPosition.lng() },
        title: this.markerTitle,
        draggable: this.draggable,
        details: this.markerDescription
      })
    );
    this.editOverlays.push(
      new google.maps.Marker({
        position: { lat: this.selectedPosition.lat(), lng: this.selectedPosition.lng() },
        title: this.markerTitle,
        draggable: this.draggable,
        details: this.markerDescription
      })
    );

    this.markerTitle = null;
    this.dialogVisible = false;
  }

  handleDragEnd(event) {
    this.messageService.add({ severity: 'info', summary: 'Marker Dragged', detail: event.overlay.getTitle() });
  }

  zoomIn(map) {
    map.setZoom(map.getZoom() + 1);
  }

  zoomOut(map) {
    map.setZoom(map.getZoom() - 1);
  }

  clear() {
    this.overlays = [];
  }

  mapperMarkersToSubEvents() {
    for (let index = 0; index < this.overlays.length; index++) {
      const lat: number = this.overlays[index].getPosition().lat();
      const lng: number = this.overlays[index].getPosition().lng();
      const title: string = this.overlays[index]['title'];
      const details: string = this.overlays[index]['details'];

      this.subEvents.push(new SubEvent(1, title, details, lat, lng));
    }

    for (let index = 0; index < this.editOverlays.length; index++) {
      const lat: number = this.editOverlays[index].getPosition().lat();
      const lng: number = this.editOverlays[index].getPosition().lng();
      const title: string = this.editOverlays[index]['title'];
      const details: string = this.editOverlays[index]['details'];

      this.editSubEvents.push(new SubEvent(1, title, details, lat, lng));
    }
  }

  initOverlays() {
    this.editEvent.subEvents.forEach(subEvent => {
      this.overlays.push(
        new google.maps.Marker({
          position: { lat: subEvent.lat, lng: subEvent.lng },
          title: subEvent.title,
          details: subEvent.details
        })
      );
    });
  }
}
