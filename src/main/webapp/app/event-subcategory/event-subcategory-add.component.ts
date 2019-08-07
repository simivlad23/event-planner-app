import { Component, OnInit } from '@angular/core';
import { EventSubcategoryService } from 'app/event-subcategory/event-subcategory.service';
import { FormBuilder, Validators } from '@angular/forms';
import { EventCategory } from 'app/event/event-category.model';
import { CategorySubcategoryService } from 'app/event-subcategory/category-subcategory.service';
import { EventSubcategory, IEventSubcategory } from 'app/event-subcategory/event-subcategory.model';

@Component({
  selector: 'jhi-event-subcategory-add',
  templateUrl: './event-subcategory-add.component.html',
  styleUrls: ['./event-subcategory-add.component.scss']
})
export class EventSubcategoryAddComponent implements OnInit {
  isSaving: boolean;

  successful: boolean;

  category: EventCategory;

  subcategory: IEventSubcategory = new EventSubcategory();

  subcategoryForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]]
  });
  unsuccessful: boolean;

  constructor(private service: EventSubcategoryService, private fb: FormBuilder, private commonService: CategorySubcategoryService) {}

  ngOnInit() {
    this.unsuccessful = false;
    this.isSaving = false;
    this.successful = false;
    this.category = new EventCategory(this.commonService.getId());
  }

  save() {
    this.isSaving = true;
    this.subcategory = this.createFromForm();
    this.service.create(this.subcategory).subscribe(
      () => {
        this.unsuccessful = false;
        this.successful = true;
        this.commonService.changeMessage(this.subcategory.name);
      },
      () => {
        this.successful = false;
        this.unsuccessful = true;
      }
    );
    this.isSaving = false;
  }

  createFromForm(): IEventSubcategory {
    return {
      id: null,
      name: this.subcategoryForm.get(['name']).value,
      eventCategory: this.category
    };
  }
}
