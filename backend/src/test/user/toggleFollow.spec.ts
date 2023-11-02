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

describe('update password test', () => {
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
      name: 'hi',
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
      .put('/user/follow/1')
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('팔로우하기', async () => {
    const beforeCount = await FollowEntity.count({});

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(await FollowEntity.count({})).toBe(beforeCount + 1);
  });

  it('언팔로우하기', async () => {
    await FollowEntity.create({
      user,
      follower: anotherUser,
    }).save();

    const beforeCount = await FollowEntity.count({});

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(await FollowEntity.count({})).toBe(beforeCount - 1);
  });

  it('토글하기', async () => {
    const beforeCount = await FollowEntity.count({});

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(await FollowEntity.count({})).toBe(beforeCount);
  });

  it('자기 자신은 안된다', async () => {
    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${user.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.FORBIDDEN);
  });

  it('토글세번', async () => {
    const beforeCount = await FollowEntity.count({});

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    await supertest(testServer.getHttpServer())
      .put(`/user/follow/${anotherUser.id}`)
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    expect(await FollowEntity.count({})).toBe(beforeCount + 1);
  });
});
