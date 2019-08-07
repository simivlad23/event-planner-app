import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class CategorySubcategoryService {
  private id = 0;

  private messageSource = new BehaviorSubject('default message');
  currentMessage = this.messageSource.asObservable();

  constructor() {}

  public getId(): number {
    return this.id;
  }

  public setId(newId: number): void {
    this.id = newId;
  }

  changeMessage(message: string) {
    this.messageSource.next(message);
  }
}
