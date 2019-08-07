import { Component, Input, OnInit } from '@angular/core';
import { CommentsService } from 'app/comments/comments.service';
import { IComment } from 'app/comments/comment.model';
import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { FormBuilder, Validators } from '@angular/forms';
import { User } from 'app/add-user-group/User';
import { Event } from 'app/event/event.model';

@Component({
  selector: 'jhi-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {
  comments: IComment[];

  @Input() _event: Event;

  user: User;

  isSaving = false;

  commentForm = this.fb.group({
    text: ['', [Validators.required]]
  });

  constructor(private commentsService: CommentsService, private fb: FormBuilder) {}

  ngOnInit() {
    this.loadComments();

    this.commentsService.getUserLogged().subscribe((resp: User) => {
      this.user = resp;
    });
  }

  loadComments() {
    this.commentsService
      .findAllByEventId(this._event.id)
      .pipe(
        filter((res: HttpResponse<IComment[]>) => res.ok),
        map((res: HttpResponse<IComment[]>) => res.body)
      )
      .subscribe((res: IComment[]) => {
        this.comments = res;
      });
  }

  save() {
    this.isSaving = true;
    const comment = this.createFromForm();
    this.commentsService.create(comment).subscribe(() => {
      this.loadComments();
      this.commentForm.controls['text'].setValue('');
    });
    this.isSaving = false;
  }

  delete(id: number) {
    this.commentsService.delete(id).subscribe(() => this.loadComments());
  }

  createFromForm(): IComment {
    return {
      id: null,
      text: this.commentForm.get(['text']).value,
      dateTime: null,
      user: this.user,
      event: this._event
    };
  }
}
