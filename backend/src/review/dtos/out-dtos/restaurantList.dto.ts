import { RestaurantDto } from './restaurant.dto';
import { RestaurantEntity } from '../../models/restaurant.entity';

export class RestaurantListDto {
  private restaurantList: RestaurantDto[];

  constructor(restaurantList: RestaurantEntity[]) {
    this.restaurantList = restaurantList.map(
      (restaurant) => new RestaurantDto(restaurant),
    );
  }
}
