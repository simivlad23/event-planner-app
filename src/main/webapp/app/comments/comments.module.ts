import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentsComponent } from 'app/comments/comments.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AvatarModule } from 'ngx-avatar';
import { NgJhipsterModule } from 'ng-jhipster';

@NgModule({
  declarations: [CommentsComponent],
  imports: [CommonModule, ReactiveFormsModule, AvatarModule, NgJhipsterModule],
  exports: [CommentsComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CommentsModule {}
