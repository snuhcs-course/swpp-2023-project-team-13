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
import { validateReviewList } from './validateReviewList';
import { ImageEntity } from '../../review/models/image.entity';
import { validateDtoKeys } from '../utils';

describe('Review test', () => {
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
    });
  });

  describe('image upload', () => {
    it('Unauthorized', async () => {
      await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .expect(HttpStatus.UNAUTHORIZED);
    });

    it('파일은 한개만', async () => {
      await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .set('Authorization', `Bearer ${accessToken}`)
        .attach('file', 'src/test/static/test1.png')
        .attach('file', 'src/test/static/test2.png')
        .expect(HttpStatus.BAD_REQUEST);
    });

    it('SUCCESS', async () => {
      await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .set('Authorization', `Bearer ${accessToken}`)
        .attach('file', 'src/test/static/test1.png')
        .expect(HttpStatus.CREATED);
    });

    it('DTO check', async () => {
      const { body } = await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .set('Authorization', `Bearer ${accessToken}`)
        .attach('file', 'src/test/static/test1.png')
        .expect(HttpStatus.CREATED);

      validateDtoKeys(body, ['id', 'url', 'isReceiptVerified']);
    });

    it('Count', async () => {
      await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .set('Authorization', `Bearer ${accessToken}`)

        .attach('file', 'src/test/static/test1.png')
        .expect(HttpStatus.CREATED);

      await supertest(testServer.getHttpServer())
        .post('/reviews/images')
        .set('Authorization', `Bearer ${accessToken}`)
        .attach('file', 'src/test/static/test1.png')
        .expect(HttpStatus.CREATED);

      expect(await ImageEntity.count()).toBe(3);
    });
  });
});
