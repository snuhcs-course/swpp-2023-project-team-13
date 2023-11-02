import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { RestaurantEntity } from '../../review/models/restaurant.entity';
import { RestaurantFixture } from '../fixture/restaurant.fixture';
import { ReviewEntity } from '../../review/models/review.entity';
import { ReviewFixture } from '../fixture/review.fixture';
import { ImageFixture } from '../fixture/image.fixture';
import {
  validateRestaurantList,
  validateReview,
  validateReviewList,
} from './validateReviewList';

describe('Get adjacent restaurant test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;
  let anotherUser: UserEntity;
  let accessToken: string;
  let restaurant: RestaurantEntity;
  let review: ReviewEntity;

  beforeAll(async () => {
    const module = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    testServer = module.createNestApplication<NestExpressApplication>();
    dataSource = testServer.get(DataSource);
    await dataSource.synchronize(true);
    appSetting(testServer);

    await testServer.init();
  });

  beforeEach(async () => {
    await dataSource.synchronize(true);

    user = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });

    anotherUser = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });

    const { body } = await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hello',
        password: 'world',
      })
      .expect(HttpStatus.CREATED);

    accessToken = body.accessToken;

    restaurant = await RestaurantFixture.create({});
    const image = await ImageFixture.create({});
    review = await ReviewFixture.create({
      restaurant,
      images: [image],
      user: anotherUser,
    });

    const anotherRestaurant = await RestaurantFixture.create({});
    review = await ReviewFixture.create({
      restaurant: anotherRestaurant,
      images: [image],
      user,
    });

    await user.follow(anotherUser);
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('DTO check', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    validateRestaurantList(body);
    expect(body.restaurantList.length).toBe(1);
  });

  it('친구 끊으면 나오지 않는다', async () => {
    const follow = await user.findFollow(anotherUser);
    await user.unfollow(follow!);
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(body.restaurantList.length).toBe(0);
  });

  it('제 3의 유저의 리뷰 레스토랑은 나오지 않는다', async () => {
    const newUser = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });

    const newRestaurant = await RestaurantFixture.create({});
    const image = await ImageFixture.create({});
    review = await ReviewFixture.create({
      restaurant: newRestaurant,
      images: [image],
      user: newUser,
    });

    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
    expect(body.restaurantList.length).toBe(1);
  });

  it('제 3의 유저의 리뷰 레스토랑은 나오지 않는다 - 친구하면 나온다', async () => {
    const newUser = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });

    const newRestaurant = await RestaurantFixture.create({});
    const image = await ImageFixture.create({});
    review = await ReviewFixture.create({
      restaurant: newRestaurant,
      images: [image],
      user: newUser,
    });

    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
    expect(body.restaurantList.length).toBe(1);

    await user.follow(newUser);

    const { body: newBody } = await supertest(testServer.getHttpServer())
      .get(`/reviews/friends/restaurants`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
    expect(newBody.restaurantList.length).toBe(2);
  });
});
