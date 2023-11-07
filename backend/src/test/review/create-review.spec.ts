import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { validateDtoKeys } from '../utils';
import { RestaurantEntity } from '../../review/models/restaurant.entity';
import { RestaurantFixture } from '../fixture/restaurant.fixture';
import { ReviewEntity } from '../../review/models/review.entity';
import axios from 'axios';
import { getReceiptOcr, getReviewIsPositive } from '../../review/ml-remote';
import { ImageFixture } from '../fixture/image.fixture';

// mock ml-remote.ts
jest.mock('../../review/ml-remote.ts');

describe('Create Review test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;
  let accessToken: string;

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

    jest.resetAllMocks();

    (getReceiptOcr as jest.Mock).mockResolvedValue({
      title: '김태준의 탕탕집',
      address: '서울 강남구 학동로4길 12. 1,2층(논현동)',
      date: '2018/01/30',
      menu: ['낙지탕탕이'],
    });
    (getReviewIsPositive as jest.Mock).mockResolvedValue(true);
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
      })
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('CREATED', async () => {
    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
        imageIds: [],
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.CREATED);

    const reviewEntity = await ReviewEntity.findOneOrFail({
      where: {
        content: 'content',
      },
      relations: {
        restaurant: true,
        images: true,
        user: true,
      },
    });

    expect(reviewEntity.content).toEqual('content');
    expect(reviewEntity.restaurant.googleMapPlaceId).toEqual(
      'ChIJN1t_tDeuEmsRUsoyG83frY4',
    );
    expect(reviewEntity.restaurant.latitude).toEqual(37.4224764);
    expect(reviewEntity.restaurant.longitude).toEqual(-122.0842499);
    expect(reviewEntity.user.id).toEqual(user.id);
  });

  it('긍정부정 / 영수증 반영', async () => {
    const image = await ImageFixture.create({});

    const receiptImageId = await ImageFixture.create({});

    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
        imageIds: [image.id],
        receiptImageId: receiptImageId.id,
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.CREATED);

    const reviewEntity = await ReviewEntity.findOneOrFail({
      where: {
        content: 'content',
      },
      relations: {
        restaurant: true,
        images: true,
        user: true,
      },
    });

    expect(reviewEntity.content).toEqual('content');
    expect(reviewEntity.restaurant.googleMapPlaceId).toEqual(
      'ChIJN1t_tDeuEmsRUsoyG83frY4',
    );
    expect(reviewEntity.restaurant.latitude).toEqual(37.4224764);
    expect(reviewEntity.restaurant.longitude).toEqual(-122.0842499);
    expect(reviewEntity.user.id).toEqual(user.id);
    expect(reviewEntity.isPositive).toEqual(true);
    expect(reviewEntity.menu).toEqual(['낙지탕탕이']);
  });

  it('영수증 에러나면 400', async () => {
    const image = await ImageFixture.create({});

    const receiptImageId = await ImageFixture.create({});

    jest.clearAllMocks();
    (getReceiptOcr as jest.Mock).mockRejectedValue(new Error('error'));

    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
        imageIds: [image.id],
        receiptImageId: receiptImageId.id,
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('영수증 에러나면 400', async () => {
    const image = await ImageFixture.create({});

    const receiptImageId = await ImageFixture.create({});

    jest.clearAllMocks();
    (getReviewIsPositive as jest.Mock).mockRejectedValue(new Error('error'));

    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
        imageIds: [image.id],
        receiptImageId: receiptImageId.id,
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('레스토랑 없었으면 생성한다.', async () => {
    const restaurantCount = await RestaurantEntity.count({});
    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
          latitude: 37.4224764,
          longitude: -122.0842499,
          name: '두근두근쭈꾸미',
        },
        content: 'content',
        imageIds: [],
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.CREATED);

    expect(await RestaurantEntity.count({})).toBe(restaurantCount + 1);
  });

  it('레스토랑 있었으면 생성하지 않는다.', async () => {
    const restaurant = await RestaurantFixture.create({});
    const restaurantCount = await RestaurantEntity.count({});

    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        restaurant: {
          googleMapPlaceId: restaurant.googleMapPlaceId,
          latitude: restaurant.latitude,
          name: '두근두근쭈꾸미',
          longitude: restaurant.longitude,
        },
        content: 'content',
        imageIds: [],
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.CREATED);

    expect(await RestaurantEntity.count({})).toBe(restaurantCount);
  });

  it.each([
    {
      restaurant: {
        googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
        name: '두근두근쭈꾸미',
        latitude: 37.4224764,
      },
      content: 'content',
      imageIds: [],
    },
    {
      restaurant: {
        googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
        latitude: 37.4224764,
        name: '두근두근쭈꾸미',
        longitude: -122.0842499,
      },
      imageIds: [],
    },
    {
      restaurant: {
        googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
        name: '두근두근쭈꾸미',
        latitude: 37.4224764,
        longitude: -122.0842499,
      },
      content: 'content',
    },
    {
      restaurant: {
        googleMapPlaceId: 'ChIJN1t_tDeuEmsRUsoyG83frY4',
        latitude: 37.4224764,
        longitude: -122.0842499,
      },
      content: 'content',
      imageIds: [1, 2, 3],
    },
  ])('input validation', async (requestBody) => {
    await supertest(testServer.getHttpServer())
      .post('/reviews')
      .send({
        ...requestBody,
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.BAD_REQUEST);
  });
});
