import { IsNumber, IsString } from 'class-validator';

export class RestaurantDto {
  @IsString()
  googleMapPlaceId: string;

  @IsNumber()
  latitude: number;

  @IsNumber()
  longitude: number;
}
