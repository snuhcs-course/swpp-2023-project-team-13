import { RestaurantEntity } from '../../models/restaurant.entity';

export class RestaurantDto {
  private id: number;
  private googleMapPlaceId: string;
  private longitude: number;
  private latitude: number;
  private name: string;

  constructor({
    id,
    googleMapPlaceId,
    longitude,
    latitude,
    name,
  }: RestaurantEntity) {
    this.id = id;
    this.googleMapPlaceId = googleMapPlaceId;
    this.longitude = longitude;
    this.latitude = latitude;
    this.name = name;
  }
}
