import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource, MoreThan } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { validateDtoKeys } from '../utils';
import { validateUserSummary } from '../review/validateReviewList';

describe('update password test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;
  let users: UserEntity[];
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

    await UserFixture.create({
      name: 'hi123',
      username: 'hellob',
      password: 'world',
    });

    await UserFixture.create({
      name: 'hi1515',
      username: 'helloc',
      password: 'world',
    });

    await UserFixture.create({
      name: 'hello',
      username: 'hellod',
      password: 'world',
    });

    await UserFixture.create({
      name: 'hiasdfasa',
      username: 'helloe',
      password: 'world',
    });

    await UserFixture.create({
      name: 'hiasdfasasdfsdfa',
      username: 'hellof',
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

    users = await UserEntity.findBy({ id: MoreThan(0) });

    for (const lhs of users) {
      for (const rhs of users) {
        if (lhs.id === rhs.id) continue;
        await lhs.follow(rhs);
      }
    }
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get('/user/follow/1')
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get(`/user/follow/${user.id}`)
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);
  });

  it('DTO check', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/user/follow/${user.id}`)
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);

    validateDtoKeys(body, ['followings', 'followers']);
    for (const user of body.followings) {
      validateUserSummary(user);
    }
    for (const user of body.followers) {
      validateUserSummary(user);
    }

    expect(body.followings.length).toBe(5);
    expect(body.followers.length).toBe(5);
  });

  it('언팔하면 반영된다', async () => {
    const follow = await user.findFollow(users[4]);
    await user.unfollow(follow!);
    const { body } = await supertest(testServer.getHttpServer())
      .get(`/user/follow/${user.id}`)
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);

    expect(body.followings.length).toBe(4);
    expect(body.followers.length).toBe(5);

    const { accessToken: anotherToken } = users[4].createToken();

    const { body: anotherBody } = await supertest(testServer.getHttpServer())
      .get(`/user/follow/${users[4].id}`)
      .set('Authorization', 'Bearer ' + anotherToken)
      .expect(HttpStatus.OK);

    expect(anotherBody.followings.length).toBe(5);
    expect(anotherBody.followers.length).toBe(4);
  });
});
