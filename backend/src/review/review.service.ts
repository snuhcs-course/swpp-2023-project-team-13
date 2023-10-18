import { Injectable } from '@nestjs/common';
import { UserEntity } from '../user/models/user.entity';
import { CreateReviewDto } from './dtos/in-dtos/createReview.dto';
import { RestaurantRepository } from './repositories/restaurant.repository';
import { ImageRepository } from './repositories/image.repository';
import { In } from 'typeorm';
import { ReviewEntity } from './models/review.entity';

@Injectable()
export class ReviewService {
  constructor(
    private readonly restaurantRepository: RestaurantRepository,
    private readonly imageRepository: ImageRepository,
  ) {}
  async create(user: UserEntity, createReviewDto: CreateReviewDto) {
    const {
      content,
      imageIds,
      receiptImageId,
      restaurant: restaurantDto,
    } = createReviewDto;

    const restaurant =
      await this.restaurantRepository.findOrCreate(restaurantDto);
    const images = await this.imageRepository.findBy({
      id: In(imageIds.concat([receiptImageId])),
    });
    const receiptImage = images.find((image) => image.id === receiptImageId);
    if (receiptImage) {
      await receiptImage.markAsReceipt();
    }

    return await ReviewEntity.create({
      content,
      user,
      restaurant,
      images,
    }).save();
  }
}
