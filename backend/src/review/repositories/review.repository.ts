import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { ReviewEntity } from '../models/review.entity';
import { In, Repository } from 'typeorm';
import { FindManyOptions } from 'typeorm/find-options/FindManyOptions';
import { FindOptionsWhere } from 'typeorm/find-options/FindOptionsWhere';
import { UserEntity } from '../../user/models/user.entity';
import { FollowEntity } from '../../user/models/follow.entity';

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

  async findReviewOfFriends(user: UserEntity) {
    const friendsIds = (
      await FollowEntity.find({
        where: {
          user: {
            id: user.id,
          },
        },
        relations: {
          follower: true,
        },
      })
    ).map((follow) => follow.follower.id);

    return this.findFull({
      user: {
        id: In([...friendsIds, user.id]),
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
