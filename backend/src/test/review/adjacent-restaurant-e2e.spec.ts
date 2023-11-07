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
      user,
    });
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get(
        `/reviews/adjacent/restaurants?longitude=127.0&latitude=37.0&distance=1`,
      )
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(
        `/reviews/adjacent/restaurants?longitude=127.0&latitude=37.0&distance=1`,
      )
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('DTO check', async () => {
    restaurant.longitude = 127.0;
    restaurant.latitude = 37.0;
    await restaurant.save();

    const { body } = await supertest(testServer.getHttpServer())
      .get(
        `/reviews/adjacent/restaurants?longitude=127.0&latitude=37.0&distance=1`,
      )
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    validateRestaurantList(body);
  });

  it('멀리 떨어지면 안잡한다', async () => {
    restaurant.longitude = 127.0;
    restaurant.latitude = 37.018018;
    await restaurant.save();

    const { body } = await supertest(testServer.getHttpServer())
      .get(
        `/reviews/adjacent/restaurants?longitude=127.0&latitude=37.0&distance=1`,
      )
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(body.restaurantList.length).toEqual(0);

    const { body: body2 } = await supertest(testServer.getHttpServer())
      .get(
        `/reviews/adjacent/restaurants?longitude=127.0&latitude=37.0&distance=3`,
      )
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(body2.restaurantList.length).toEqual(1);
  });
});
