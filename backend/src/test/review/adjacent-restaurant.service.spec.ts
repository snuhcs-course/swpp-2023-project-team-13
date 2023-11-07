import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { UserEntity } from '../../user/models/user.entity';
import { RestaurantEntity } from '../../review/models/restaurant.entity';
import { ReviewService } from '../../review/review.service';
import { plainToInstance } from 'class-transformer';
import { ReviewAdjacentQueryDto } from '../../review/dtos/in-dtos/review-adjacent-query.dto';

describe('Get adjacent restaurant test', () => {
  let testServer: NestExpressApplication;
  let reviewService: ReviewService;
  let restaurant: RestaurantEntity;

  beforeAll(async () => {
    const module = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();
    restaurant = RestaurantEntity.create({
      googleMapPlaceId: '123',
      longitude: 127.018018,
      latitude: 37.018018,
    });
    restaurant = RestaurantEntity.create({
      googleMapPlaceId: '123',
      longitude: 127.018018,
      latitude: 50.018018,
    });
    reviewService = module.get(ReviewService);
  });

  beforeEach(async () => {});

  it('멀리 떨어지면 안잡한다', async () => {
    const restaurants = [
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.018018,
        latitude: 37.018018,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.018018,
        latitude: 50.018018,
      }),
    ];

    const adjacentRestaurants = reviewService.getAdjacentRestaurant(
      plainToInstance(ReviewAdjacentQueryDto, {
        longitude: 127.018018,
        latitude: 37.018018,
        distance: 1,
      }),
      restaurants,
    );

    expect(adjacentRestaurants.length).toBe(1);
  });

  it('멀리 떨어지면 안잡한다', async () => {
    const restaurants = [
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.018018,
        latitude: 37.018018,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.018018,
        latitude: 50.018018,
      }),
    ];

    const adjacentRestaurants = reviewService.getAdjacentRestaurant(
      plainToInstance(ReviewAdjacentQueryDto, {
        longitude: 127.018018,
        latitude: 37.018018,
        distance: 1,
      }),
      restaurants,
    );

    expect(adjacentRestaurants.length).toBe(1);
  });

  it('거리 세밀한 체크 2.5km내', async () => {
    const restaurants = [
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.046107,
        latitude: 37.018018,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.018018,
        latitude: 36.995496,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 126.989929,
        latitude: 37.018018,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 127.057343,
        latitude: 37.018018,
      }),
      RestaurantEntity.create({
        googleMapPlaceId: '123',
        longitude: 126.978693,
        latitude: 37.018018,
      }),
    ];

    const adjacentRestaurants = reviewService.getAdjacentRestaurant(
      plainToInstance(ReviewAdjacentQueryDto, {
        longitude: 127.018018,
        latitude: 37.018018,
        distance: 3,
      }),
      restaurants,
    );

    expect(adjacentRestaurants.length).toBe(3);
  });
});
