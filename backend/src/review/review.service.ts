import { Injectable } from '@nestjs/common';
import { UserEntity } from '../user/models/user.entity';
import { CreateReviewDto } from './dtos/in-dtos/createReview.dto';
import { RestaurantRepository } from './repositories/restaurant.repository';
import { ImageRepository } from './repositories/image.repository';
import { In } from 'typeorm';
import { ReviewEntity } from './models/review.entity';
import { ReviewAdjacentQueryDto } from './dtos/in-dtos/review-adjacent-query.dto';
import { getDistance } from 'geolib';
// import axios from 'axios';

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
    if (receiptImage) {
      await receiptImage.markAsReceipt();
      // const receiptData = (
      //   await axios.post(`${process.env.ML_URL}ocr`, {
      //     review: content,
      //   })
      // ).data;
    }

    // const isPositive = (
    //   await axios.post(`${process.env.ML_URL}review`, {
    //     review: content,
    //   })
    // ).data['result'];

    return await ReviewEntity.create({
      content,
      user,
      restaurant,
      images,
      // isPositive,
    }).save();
  }

  async getAdjacentRestaurant(data: ReviewAdjacentQueryDto) {
    const { longitude, latitude, distance } = data;
    const restaurants = await this.restaurantRepository.find({});
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
