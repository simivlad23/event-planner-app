import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { EventCategoryComponent } from 'app/event-category/event-category.component';
import { EventPlannerSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { eventCategoriesRoute } from 'app/event-category/event-category.route';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule, MatExpansionModule, MatInputModule, MatTreeModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CategorySubcategoryService } from 'app/event-subcategory';

@NgModule({
  imports: [
    EventPlannerSharedModule,
    RouterModule.forChild([eventCategoriesRoute]),

    BrowserAnimationsModule,
    MatExpansionModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatTreeModule,
    MatCardModule
  ],
  providers: [CategorySubcategoryService],
  declarations: [EventCategoryComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventCategoryModule {}
