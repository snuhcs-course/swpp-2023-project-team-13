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
import { validateUserSummary } from '../review/validateReviewList';

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
      username: 'hellof',
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
  });

  it('unauthorized', async () => {
    await supertest(testServer.getHttpServer())
      .get('/user/search/hello')
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('OK', async () => {
    await supertest(testServer.getHttpServer())
      .get('/user/search/hello')
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);
  });

  it('안찾아지면 랜덤 5개', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get('/user/search/hell123123213o')
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);

    expect(body.userList.length).toBe(5);
  });

  it('DTO check', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get('/user/search/hello')
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);

    validateDtoKeys(body, ['userList']);

    for (const user of body.userList) {
      validateUserSummary(user);
    }
  });

  it('유사도 정렬', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .get('/user/search/hi')
      .set('Authorization', 'Bearer ' + accessToken)
      .expect(HttpStatus.OK);

    validateDtoKeys(body, ['userList']);

    expect(body.userList.length).toBe(4);
    expect(body.userList[0].name).toBe('hi');
    expect(body.userList[1].name).toBe('hi123');
    expect(body.userList[2].name).toBe('hi1515');
    expect(body.userList[3].name).toBe('hiasdfasa');
  });
});
