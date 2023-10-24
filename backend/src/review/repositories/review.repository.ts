import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { ReviewEntity } from '../models/review.entity';
import { Repository } from 'typeorm';
import { FindManyOptions } from 'typeorm/find-options/FindManyOptions';
import { FindOptionsWhere } from 'typeorm/find-options/FindOptionsWhere';
import { UserEntity } from '../../user/models/user.entity';

@CustomRepository(ReviewEntity)
export class ReviewRepository extends Repository<ReviewEntity> {
  findOfRestaurantId(restaurantId: string) {
    return this.findFull({
      restaurant: {
        googleMapPlaceId: restaurantId,
      },
    });
  }

  findOfReviewId(reviewId: number) {
    return this.findFullOne({
      id: reviewId,
    });
  }

  private async findFullOne(options: FindOptionsWhere<ReviewEntity>) {
    const find = await this.find({
      where: options,
      relations: {
        images: true,
        restaurant: true,
        user: true,
      },
    });

    if (find.length === 0) return null;

    return find[0];
  }

  findFull(options: FindOptionsWhere<ReviewEntity>) {
    return this.find({
      where: options,
      relations: {
        images: true,
        user: true,
        restaurant: true,
      },
      order: {
        id: 'DESC',
      },
    });
  }

  findOfUser(user: UserEntity) {
    return this.findFull({
      user: {
        id: user.id,
      },
    });
  }

  findRandomReviews(limit: number) {
    return this.createQueryBuilder('review')
      .leftJoinAndSelect('review.images', 'image')
      .leftJoinAndSelect('review.user', 'user')
      .leftJoinAndSelect('review.restaurant', 'restaurant')
      .orderBy('RANDOM()')
      .limit(limit)
      .getMany();
  }
}
