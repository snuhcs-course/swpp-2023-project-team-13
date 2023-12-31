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
      .get(`/reviews/my`)
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/reviews/my`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('DTO check', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/my`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    validateReviewList(body);
  });

  it('리뷰 없으면 빈 배열 준다', async () => {
    const newUser = await UserFixture.create({
      name: 'hinew',
      username: 'hellonew',
      password: 'world',
    });

    const { body: authBody } = await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hellonew',
        password: 'world',
      })
      .expect(HttpStatus.CREATED);

    accessToken = authBody.accessToken;

    const { body } = await supertest(testServer.getHttpServer())
      .get(`/reviews/my`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(body.reviewList).toStrictEqual([]);
  });

  it('should fetch user reviews in time order', async () => {
    // Create multiple reviews for the user with different timestamps
    const review1 = await ReviewFixture.create({
      restaurant,
      images: [await ImageFixture.create({})],
      user,
    });
    const review2 = await ReviewFixture.create({
      restaurant,
      images: [await ImageFixture.create({})],
      user,
    });

    // Fetch the user's reviews
    const { body } = await supertest(testServer.getHttpServer())
      .get('/reviews/my')
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    // Check that the reviews are in time order
    expect(body.reviewList[0].id).toBe(review2.id);
    expect(body.reviewList[1].id).toBe(review1.id);
    expect(body.reviewList[2].id).toBe(review.id);
  });
});
