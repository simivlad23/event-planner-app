import { Component, DoCheck, OnInit } from '@angular/core';
import { GroupPageService } from 'app/group-page/group-page.service';
import { User } from 'app/add-user-group/User';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AddUsersToGroupComponent } from 'app/group-page/add-users-to-group/add-users-to-group.component';
import { DomSanitizer } from '@angular/platform-browser';
import allLocales from '@fullcalendar/core/locales-all';

import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-group-page',
  templateUrl: './group-page.component.html',
  styleUrls: ['./group-page.component.scss']
})
export class GroupPageComponent implements OnInit, DoCheck {
  allUsers: User[];
  showUsers: boolean;
  groupId: number;
  group: any;
  allLocales = allLocales;
  events: any[];

  header: {
    left: 'prev,next';
    center: 'title';
    right: 'dayGridMonth,timeGridWeek,timeGridDay';
  };
  calendarPlugins = [dayGridPlugin, timeGridPlugin, interactionPlugin];
  language: string;
  todaybutton: string;
  tooltipText: string;
  tooltipStyle: any;

  constructor(
    private groupPageService: GroupPageService,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private router: Router,
    private sessionStorage: SessionStorageService,
    private sanitization: DomSanitizer
  ) {}

  ngOnInit() {
    this.todaybutton = 'Today';
    this.tooltipText = ' ';
    this.tooltipStyle = ' ';
    this.showUsers = false;
    this.route.params.subscribe(params => {
      this.groupId = params['id'];
      this.groupPageService.groupId = this.groupId; // setting group id for the add popup
      this.groupPageService.getGroupById(this.groupId).subscribe(resp => {
        this.group = resp.body;
        this.getAllEvents();
      });
    });
    this.language = this.sessionStorage.retrieve('locale');
    this.changeButtonText(this.language);
  }

  ngDoCheck() {
    this.language = this.sessionStorage.retrieve('locale');
    this.changeButtonText(this.language);
  }

  getAllEvents() {
    const newEvents = [];

    this.groupPageService.getEvents(this.groupId).subscribe(events => {
      // {id: number, title: string, dateTime: any}
      for (const event of events.body) {
        newEvents.push({
          eventId: event.id,
          description: event.description,
          title: event.title,
          date: event.dateTime
        });
      }
      this.events = newEvents;
    });
  }

  getAllUsers() {
    this.groupPageService.getAllUser(this.groupId).subscribe(resp => {
      this.allUsers = resp.body;
    });
  }

  showAllUsers() {
    this.getAllUsers();
    this.showUsers = !this.showUsers;
  }

  addUserToGroup() {
    this.modalService.open(AddUsersToGroupComponent).result.then();
  }

  navigateTo(event: any) {
    // todo: cate pagina evenimentului

    this.router.navigateByUrl('/events/' + event.event.extendedProps.eventId);
  }

  details() {
    this.language = this.sessionStorage.retrieve('locale');
    console.log(this.language);
  }

  // showPopupDescription(event: any) {
  //   // dynamically add bootstrap to the selected event todo: find re render
  //    event.el.querySelectorAll(".fc-content")[0].setAttribute("[ngbTooltip]", event.event.extendedProps.description);
  //    event.el.querySelectorAll(".fc-content")[0].setAttribute("placement", "top");
  //    console.log("se apeleaza");
  //    console.log(event.el.querySelectorAll(".fc-content")[0]);
  // }

  private changeButtonText(lang: string) {
    if (lang === 'ro') {
      this.todaybutton = 'Azi';
    }
    if (lang === 'en') {
      this.todaybutton = 'Today';
    }
  }

  // showTooltip(event: any) {
  //   console.log(event.jsEvent.x);
  //   console.log(event.jsEvent.y);
  //   let div = document.getElementById('tool');
  //   console.log(div);
  //   //div.hidden = false;
  //   this.tooltipText = event.event.extendedProps.description;
  //   (div as any).tooltip('toogle');
  //   this.tooltipStyle = this.sanitization.bypassSecurityTrustStyle('position: fixed; ' +
  //                       'top: ' + event.jsEvent.y + 'px; ' +
  //                       'left: ' + event.jsEvent.x + 'px;' +
  //                       'z-index: 999;');
  //
  // }
  //
  // hideTooltip(event: any) {
  //   let div = document.getElementById('tool');
  //   //div.hidden = true;
  // }
}
