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

  /*
  beforeEach(async () => {
    await dataSource.synchronize(true);

    user = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });
  });

   */

  beforeEach(async () => {
    // Add logging to check the function flow and values
    console.log('Before creating user fixture');

    const userData = {
      name: 'hi',
      username: 'hello',
      password: 'world',
    };

    try {
      // Add logging to check the user data before creation
      console.log('User data before creation:', userData);

      user = await UserFixture.create(userData);

      // Add logging to check the created user data
      console.log('User data after creation:', user);
    } catch (error) {
      // Add logging to check for any errors during data fixture creation
      console.error('Error creating user fixture:', error);
    }
  });

  it('Check if the username already exists', async () => {
    await supertest(testServer.getHttpServer())
      .post('/user')
      .send({
        name: 'John Doe',
        username: 'hello',
        password: 'password123',
      })
      .expect(HttpStatus.CONFLICT);
  });

  it('signup OK', async () => {
    const response = await supertest(testServer.getHttpServer())
      .post('/user')
      .send({
        name: 'abc',
        username: 'def',
        password: 'ghi',
      })
      .expect(HttpStatus.CREATED);
  });

  afterAll(async () => {
    await dataSource.synchronize(true);

    await testServer.close();
  });
});
