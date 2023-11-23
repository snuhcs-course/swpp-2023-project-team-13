import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { FollowEntity } from '../../user/models/follow.entity';
import { validateDtoKeys } from '../utils';

describe('getUserMe test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;
  let accessToken: string;
  let anotherUser: UserEntity;

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
      name: '황승준',
      username: 'hello',
      password: 'world',
    });

    anotherUser = await UserFixture.create({
      name: 'hi',
      username: 'helloasdfasdfasdf',
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
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get('/user/me')
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/user/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('DTO test', async () => {
    await FollowEntity.create({
      user: anotherUser,
      follower: user,
    }).save();

    const { body } = await supertest(testServer.getHttpServer())
      .get(`/user/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    validateDtoKeys(body, [
      'id',
      'name',
      'username',
      'followerCount',
      'followingCount',
    ]);

    expect(body.followingCount).toBe(1);
    expect(body.followerCount).toBe(0);
  });
});
