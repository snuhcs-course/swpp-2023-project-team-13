import { BadRequestException, Injectable } from '@nestjs/common';
import { UserEntity } from '../user/models/user.entity';
import { CreateReviewDto } from './dtos/in-dtos/createReview.dto';
import { RestaurantRepository } from './repositories/restaurant.repository';
import { ImageRepository } from './repositories/image.repository';
import { In } from 'typeorm';
import { ReviewEntity } from './models/review.entity';
import { ReviewAdjacentQueryDto } from './dtos/in-dtos/review-adjacent-query.dto';
import { getDistance } from 'geolib';
import { getReceiptOcr, getReviewIsPositive } from './ml-remote';
import { RestaurantEntity } from './models/restaurant.entity';

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
      id: In(imageIds.concat([receiptImageId ?? -1])),
    });
    const receiptImage = images.find((image) => image.id === receiptImageId);
    let menu = [];
    if (receiptImage) {
      await receiptImage.markAsReceipt();
      try {
        const receiptData = await getReceiptOcr(receiptImage.url);
        menu = receiptData['menu'];
      } catch (e) {
        throw new BadRequestException('잘못된 영수증입니다.');
      }
    }

    let isPositive = false;
    try {
      isPositive = await getReviewIsPositive(content);
    } catch (e) {
      throw new BadRequestException('리뷰 분석 중 오류가 발생했습니다.');
    }

    return await ReviewEntity.create({
      content,
      user,
      restaurant,
      images,
      isPositive,
      menu,
    }).save();
  }

  getAdjacentRestaurant(
    data: ReviewAdjacentQueryDto,
    restaurants: RestaurantEntity[],
  ) {
    const { longitude, latitude, distance } = data;
    const KILOMETER = 1000;

    return restaurants.filter(
      (restaurant) =>
        getDistance(
          { latitude, longitude },
          { latitude: restaurant.latitude, longitude: restaurant.longitude },
        ) <=
        distance * KILOMETER,
    );
  }
}
