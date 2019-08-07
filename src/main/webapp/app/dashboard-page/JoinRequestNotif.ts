export interface JoinRequestNotif {
  requesterId: number;
  groupId: number;
  joinMessage: string;
}

export class JoinRequest implements JoinRequestNotif {
  requesterId: number;
  groupId: number;
  joinMessage: string;

  constructor(requesterId: number, groupId: number, joinMessage: string) {
    this.requesterId = requesterId;
    this.groupId = groupId;
    this.joinMessage = joinMessage;
  }
}
