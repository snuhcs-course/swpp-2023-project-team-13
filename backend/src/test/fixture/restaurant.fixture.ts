import { RestaurantEntity } from '../../review/models/restaurant.entity';

type RestaurantFixtureProps = {
  googleMapPlaceId: string;
  latitude: number;
  longitude: number;
};

export class RestaurantFixture {
  static create({
    googleMapPlaceId,
    latitude,
    longitude,
  }: Partial<RestaurantFixtureProps>) {
    return RestaurantEntity.create({
      googleMapPlaceId: googleMapPlaceId ?? 'googleMapPlaceId',
      latitude: latitude ?? 37.123456,
      longitude: longitude ?? 127.123456,
    }).save();
  }
}
