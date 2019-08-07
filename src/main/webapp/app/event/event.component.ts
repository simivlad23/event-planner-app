import { Component, HostListener, OnInit } from '@angular/core';
import { EventService } from 'app/event/event.service';
import { JhiAlertService } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { IEvent } from 'app/event/event.model';
import { ActivatedRoute } from '@angular/router';
import { EventCategory } from 'app/event/event-category.model';
import { UserGroup } from 'app/event/user-group.model';
import { MessageService, SelectItem } from 'primeng/api';
import { SubEvent } from 'app/event/sub-event.model';
import { EditEventService } from '../../edit-event.service';

declare var google: any;

@Component({
  selector: 'jhi-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {
  event: IEvent = {
    id: null,
    dateTime: null,
    title: null,
    description: null,
    locationType: 'ADDRESS',
    location: null,
    eventCategory: new EventCategory(),
    userGroup: new UserGroup(),
    eventSubcategories: []
  };

  subEventsList: SubEvent[] = [];

  options: any;

  overlays: any[] = [];

  dialogVisible: boolean;

  markerTitle: string;

  selectedPosition: any;

  infoWindow: any;

  draggable: boolean;

  subEvents: SelectItem[] = [];

  selectedSubEvent: string;

  screenHeight;

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
  }

  constructor(
    private eventService: EventService,
    private jhiAlertService: JhiAlertService,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private editService: EditEventService
  ) {
    this.selectedSubEvent = 'Select Sub Event';
  }

  ngOnInit() {
    this.route.params.subscribe(params => this.loadEvent(params['id']));
    this.screenHeight = window.innerHeight;

    this.options = {
      center: { lat: 46.78752375382936, lng: 23.59417819773182 },
      zoom: 17
    };

    this.infoWindow = new google.maps.InfoWindow();
  }

  handleOverlayClick(event) {
    const isMarker = event.overlay.getTitle !== undefined;

    if (isMarker) {
      const title = event.overlay.getTitle();
      this.infoWindow.setContent('' + title + '');
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
        draggable: this.draggable
      })
    );
    this.markerTitle = null;
    this.dialogVisible = false;
  }

  handleDragEnd(event) {
    this.messageService.add({ severity: 'info', summary: 'Marker Dragged', detail: event.overlay.getTitle() });
  }

  initOverlays() {
    this.subEventsList.forEach(subEvent => {
      this.overlays.push(
        new google.maps.Marker({
          position: { lat: subEvent.lat, lng: subEvent.lng },
          title: subEvent.title,
          details: subEvent.details
        })
      );
    });
  }

  zoomIn(gmap) {
    gmap.setZoom(gmap.getZoom() + 1);
  }

  zoomOut(gmap) {
    gmap.setZoom(gmap.getZoom() - 1);
  }

  clear() {}

  initDropDownList() {
    this.subEventsList.forEach(subEvent => {
      this.subEvents.push({ label: subEvent.title, value: subEvent });
    });
  }

  changeFocusSubEvent(gmap) {
    const title: string = this.selectedSubEvent['title'];
    const details: string = this.selectedSubEvent['details'];
    const lat: number = this.selectedSubEvent['lat'];
    const lng: number = this.selectedSubEvent['lng'];

    this.infoWindow.setContent('' + title + ' ' + details);
    gmap.panTo(new google.maps.LatLng(lat, lng));
  }

  loadEvent(id: number) {
    this.eventService
      .find(id)
      .pipe(
        filter((res: HttpResponse<IEvent>) => res.ok),
        map((res: HttpResponse<IEvent>) => res.body)
      )
      .subscribe((res: IEvent) => {
        this.event = res;
        this.subEventsList = res.subEvents;
        this.initOverlays();
        this.initDropDownList();
      });
  }

  editEvent() {
    this.editService.editEvent = true;
    this.editService.event = this.event;
  }
}
