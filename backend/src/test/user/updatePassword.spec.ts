import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';

describe('update password test', () => {
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
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'world',
        newPassword: 'newPassword',
      })
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'world',
        newPassword: 'newPassword',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);
  });

  it('currentPassword is not given', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        newPassword: 'newPassword',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('currentPassword is wrong', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'adsfasdfasdfs',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('currentPassword is wrong', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'adsfasdfasdfs',
        newPassword: 'newPassword',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.FORBIDDEN);
  });

  it('newPassword is hashed', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'world',
        newPassword: 'newPassword',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    const userEntity = await UserEntity.findOneByOrFail({
      id: user.id,
    });

    expect(userEntity.password).not.toEqual('newPassword');
  });

  it('바꾼 이후 new Password 통해서 로그인 가능', async () => {
    await supertest(testServer.getHttpServer())
      .patch('/user/me/password')
      .send({
        currentPassword: 'world',
        newPassword: 'newPassword',
      })
      .set('Authorization', `Bearer ${accessToken}`)
      .expect(HttpStatus.OK);

    await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hello',
        password: 'newPassword',
      })
      .expect(HttpStatus.CREATED);
  });
});
