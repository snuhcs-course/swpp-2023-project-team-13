import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { ReviewEntity } from '../models/review.entity';
import { Repository } from 'typeorm';

@CustomRepository(ReviewEntity)
export class ReviewRepository extends Repository<ReviewEntity> {
  findOfRestaurantId(restaurantId: string) {
    return this.find({
      where: { restaurant: { googleMapPlaceId: restaurantId } },
      relations: {
        images: true,
        user: true,
      },
    });
  }
}
