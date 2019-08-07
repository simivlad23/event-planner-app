export interface IMapsPosition {
  lat?: number;
  lng?: number;
}

export class MapsPosition implements IMapsPosition {
  lat?: number;
  lng?: number;

  constructor(lat?: number, lng?: number) {
    this.lat = lat;
    this.lng = lng;
  }
}
