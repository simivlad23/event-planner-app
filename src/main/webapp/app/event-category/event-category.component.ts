import { Component, OnInit } from '@angular/core';
import { EventCategoryService } from 'app/event-category/event-category.service';
import { FormBuilder, Validators } from '@angular/forms';
import * as HttpStatus from 'http-status-codes';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IEventSubcategory } from 'app/event-subcategory/event-subcategory.model';
import { IEventCategory } from 'app/event/event-category.model';
import { EventSubcategoryService } from 'app/event-subcategory/event-subcategory.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventSubcategoryAddComponent } from 'app/event-subcategory';
import { CategorySubcategoryService } from 'app/event-subcategory/category-subcategory.service';

@Component({
  selector: 'jhi-event-categories',
  templateUrl: './event-category.component.html',
  styleUrls: ['./event-category.component.scss']
})
export class EventCategoryComponent implements OnInit {
  categories: Array<IEventCategory>;
  success: boolean;
  response: string;

  modalRef: NgbModalRef;
  isOpen = false;

  responseBackEnd: Observable<HttpResponse<any>>;
  eventCategoryForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]]
  });
  panelOpenState: boolean;

  constructor(
    private service: EventCategoryService,
    private subcategoryService: EventSubcategoryService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    private commonService: CategorySubcategoryService
  ) {}

  ngOnInit() {
    this.getAllCategories();
    this.commonService.currentMessage.subscribe(() => this.loadSubcategories());
    this.success = false;
    this.response = '';
    this.panelOpenState = false;
    this.categories = new Array<any>();
  }

  getAllCategories() {
    this.responseBackEnd = this.service.getAll();
    this.responseBackEnd.subscribe(resp => {
      if (resp.status === HttpStatus.OK) {
        this.categories = resp.body;
        this.loadSubcategories();
      }
      if (resp.status === HttpStatus.CONFLICT) {
        this.categories = null;
      }
    });
  }

  loadSubcategories() {
    for (const index in this.categories) {
      if (this.categories.hasOwnProperty(index)) {
        this.subcategoryService.findByCategoryId(this.categories[index].id).subscribe((res: HttpResponse<IEventSubcategory[]>) => {
          if (res.status === HttpStatus.OK) {
            this.categories[index].subcategories = res.body;
          }
          if (res.status === HttpStatus.NO_CONTENT) {
            this.categories[index].subcategories = [];
          }
        });
      }
    }
  }

  save() {
    let categoryName = this.eventCategoryForm.get(['name']).value;
    categoryName = this.capitalizeFirstLetter(categoryName); // first letter allways upper

    if (this.categories.find(x => x.name === categoryName) === undefined) {
      const eventCategory = { name: categoryName };
      this.service.save(eventCategory).subscribe(resp => {
        switch (resp.status) {
          case HttpStatus.BAD_REQUEST: {
            this.response = 'null';
            break;
          }
          case HttpStatus.CONFLICT: {
            this.response = 'fail';
            break;
          }
          case HttpStatus.CREATED: {
            this.response = 'success';
            break;
          }
          default: {
            this.response = 'error';
          }
        }
        this.getAllCategories();
      });
    } else {
      this.response = 'fail';
    }
  }

  capitalizeFirstLetter(input) {
    return typeof input === 'string' && input.length > 0 ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : input;
  }

  trackEventCategoryById(item: IEventCategory) {
    return item.id;
  }

  addSubcategory(id: number) {
    if (this.isOpen) {
      return;
    }
    this.commonService.setId(id);
    this.modalRef = this.modalService.open(EventSubcategoryAddComponent);
    this.modalRef.result.then(
      result => {
        this.isOpen = false;
      },
      reason => {
        this.isOpen = false;
      }
    );
  }

  deleteSubcategory(id: number) {
    this.subcategoryService.delete(id).subscribe(() => this.loadSubcategories());
  }
}

export interface EventCategory {
  id: number;
  name: string;
  subcategories: IEventSubcategory[];
}
