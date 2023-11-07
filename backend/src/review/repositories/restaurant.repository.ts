import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { RestaurantEntity } from '../models/restaurant.entity';
import { Repository } from 'typeorm';
import { RestaurantDto } from '../dtos/in-dtos/restaurant.dto';

@CustomRepository(RestaurantEntity)
export class RestaurantRepository extends Repository<RestaurantEntity> {
  async findOrCreate(data: RestaurantDto) {
    const { googleMapPlaceId, longitude, latitude, name } = data;
    const restaurant = await this.findOne({
      where: { googleMapPlaceId },
    });

    if (restaurant) return restaurant;

    return await this.create({
      googleMapPlaceId,
      longitude,
      latitude,
      name,
    }).save();
  }
}
