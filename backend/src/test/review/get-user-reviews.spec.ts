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
import { validateReview, validateReviewList } from './validateReviewList';

describe('My Review test', () => {
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
      name: 'hi123',
      username: 'hellob',
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
    await ReviewFixture.create({
      restaurant,
      images: [image],
      user: anotherUser,
    });
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/reviews/users/${anotherUser.id}`)

      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/reviews/users/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('DTO check', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/users/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    validateReviewList(body);
    expect(body.reviewList.length).toBe(2);
  });

  it('리뷰 없으면 빈 배열 준다', async () => {
    const newUser = await UserFixture.create({
      name: 'hinew',
      username: 'hellonew',
      password: 'world',
    });

    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/users/${newUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(body.reviewList).toStrictEqual([]);
  });
});
