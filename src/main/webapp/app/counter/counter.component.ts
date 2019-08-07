import { Component, Input, OnInit } from '@angular/core';
import { CounterService } from 'app/counter/counter.service';

@Component({
  selector: 'jhi-counter',
  templateUrl: './counter.component.html',
  styleUrls: ['./counter.component.scss']
})
export class CounterComponent implements OnInit {
  private _yesVotes: number;
  private _noVotes: number;
  private _maybeVotes: number;
  @Input() _eventId: string;
  private _userId: number;
  private _myVote: string;

  constructor(private service: CounterService) {
    // this._yesVotes = 0;
    // this._noVotes = 0;
    // this._maybeVotes = 0;
  }

  get myVote(): string {
    return this._myVote;
  }
  get yesVotes(): number {
    return this._yesVotes;
  }
  get noVotes(): number {
    return this._noVotes;
  }
  get maybeVotes(): number {
    return this._maybeVotes;
  }

  set userId(value: number) {
    this._userId = value;
  }
  get userId() {
    return this._userId;
  }
  ngOnInit() {
    this.service.getLoggedUser().subscribe(user => {
      this._userId = user.id;
      this.getAll();
    });
  }

  getAll() {
    this.service.getYesNoMaybeCounter(this._eventId).subscribe(response => {
      this._yesVotes = response.body.yesVotes;
      this._noVotes = response.body.noVotes;
      this._maybeVotes = response.body.maybeVotes;
      this.service.getMyVote(this.userId, Number(this._eventId)).subscribe(resp => {
        this._myVote = resp.body;
      });
    });
  }

  public vote(voteType: string) {
    const voteDto = { userId: this._userId, eventId: this._eventId, type: voteType };
    this.service.save(voteDto).subscribe(x => this.getAll());
  }
}
