export class Vote {
  private _userId: number;
  private _eventId: number;
  private _nrVotes: number;
  private _type: string;

  constructor() {}

  get userId(): number {
    return this._userId;
  }

  set userId(value: number) {
    this._userId = value;
  }

  get eventId(): number {
    return this._eventId;
  }

  set eventId(value: number) {
    this._eventId = value;
  }

  get nrVotes(): number {
    return this._nrVotes;
  }

  set nrVotes(value: number) {
    this._nrVotes = value;
  }

  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }
}
