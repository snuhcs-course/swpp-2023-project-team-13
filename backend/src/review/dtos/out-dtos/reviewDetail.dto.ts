import { ImageDto } from './imageDto';
import { RestaurantDto } from './restaurant.dto';
import { UserSummaryDto } from './userSummary.dto';
import { ReviewEntity } from '../../models/review.entity';
import { UserEntity } from '../../../user/models/user.entity';

export class ReviewDetailDto {
  private id: number;
  private content: string;
  private images: ImageDto[];
  private receiptImage: ImageDto | null;
  private issuedAt: string;
  private restaurant: RestaurantDto;
  private user: UserSummaryDto;

  constructor(
    review: ReviewEntity,

    user: UserEntity,
  ) {
    const { id, content, images, receiptImage, restaurant } = review;

    this.id = id;
    this.content = content;
    this.images = images.map((image) => new ImageDto(image));
    this.receiptImage = receiptImage ? new ImageDto(receiptImage) : null;
    this.issuedAt = review.getIssuedAt().toString();
    this.restaurant = new RestaurantDto(restaurant);
    this.user = new UserSummaryDto(user);
  }
}
