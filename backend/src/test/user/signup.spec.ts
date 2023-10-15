import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';

describe('signup test', () => {
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

  it('Check if the username already exists', async () => {
    await supertest(testServer.getHttpServer())
      .post('/user/signup')
      .send({
        name: 'John Doe',
        username: 'hello',
        password: 'password123',
      })
      .expect(HttpStatus.CONFLICT);
  });

  afterAll(async () => {
    await dataSource.synchronize(true);

    await testServer.close();
  });
});
