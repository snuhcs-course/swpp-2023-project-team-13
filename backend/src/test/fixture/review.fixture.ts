import { RestaurantEntity } from '../../review/models/restaurant.entity';
import { UserEntity } from '../../user/models/user.entity';
import { ImageEntity } from '../../review/models/image.entity';
import { ReviewEntity } from '../../review/models/review.entity';
import { RestaurantFixture } from './restaurant.fixture';

type ReviewFixtureProps = {
  restaurant: RestaurantEntity;
  user: UserEntity;
  images: ImageEntity[];
  content: string;
};

export class ReviewFixture {
  static async create({
    restaurant,
    user,
    images,
    content,
  }: Partial<ReviewFixtureProps>) {
    return ReviewEntity.create({
      restaurant:
        restaurant ?? (await RestaurantFixture.create({ name: '홍길동' })),
      user,
      images: images ?? [],
      content: content ?? 'content',
    });
  }
}
