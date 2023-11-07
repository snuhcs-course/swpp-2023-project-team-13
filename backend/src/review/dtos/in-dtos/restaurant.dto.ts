import { IsNumber, IsString } from 'class-validator';

export class RestaurantDto {
  @IsString()
  googleMapPlaceId: string;

  @IsString()
  name: string;

  @IsNumber()
  latitude: number;

  @IsNumber()
  longitude: number;
}
