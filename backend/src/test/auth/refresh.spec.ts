import { NestExpressApplication } from '@nestjs/platform-express';
import { DataSource } from 'typeorm';
import { UserEntity } from '../../user/models/user.entity';
import { Test } from '@nestjs/testing';
import { AppModule } from '../../app.module';
import { appSetting } from '../../main';
import { UserFixture } from '../fixture/user.fixture';
import * as supertest from 'supertest';
import { HttpStatus } from '@nestjs/common';
import { canReferenceNode } from '@nestjs/swagger/dist/plugin/utils/plugin-utils';

describe('refresh test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;

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
  });

  it('should not generate new tokens with wrong refresh token', async () => {
    const loginResponse = await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hello',
        password: 'world',
      });
    const refreshToken = 'wrongRefreshToken';

    const refreshResponse = await supertest(testServer.getHttpServer())
      .post('/auth/refresh')
      .set('Authorization', `Bearer ${refreshToken}`)
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('should generate new tokens with current refresh token', async () => {
    const loginResponse = await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hello',
        password: 'world',
      });
    const refreshToken = loginResponse.body.refreshToken;

    const refreshResponse = await supertest(testServer.getHttpServer())
      .post('/auth/refresh')
      .set('Authorization', `Bearer ${refreshToken}`)
      .expect(HttpStatus.CREATED);

    const { body } = refreshResponse;
    expect(body.accessToken).not.toEqual(body.refreshToken);
  });
});
