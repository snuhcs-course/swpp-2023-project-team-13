import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { UserEntity } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { UserRepository } from '../../user/repostiories/user.repository';

describe('signup test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: UserEntity;
  let userRepository: UserRepository;

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
  /*

  beforeEach(async () => {
    await dataSource.synchronize(true);

    console.log('Before creating user fixture');

    let userData = await UserFixture.create({
      name: 'hi',
      username: 'hello',
      password: 'world',
    });

    try {
      console.log('User data before creation:', userData);

      user = await UserFixture.create(userData);

      console.log('User data after creation:', user);
    } catch (error) {
      console.error('Error creating user fixture:', error);
    }
  });
*/
  it('Check if the username already exists', async () => {
    await supertest(testServer.getHttpServer()).post('/user').send({
      name: 'John Doe',
      username: 'hello',
      password: 'password123',
    });
    expect(HttpStatus.CONFLICT);

    expect(await UserEntity.count()).toBe(1);
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

  /*
  afterAll(async () => {
    await dataSource.synchronize(true);

    await testServer.close();
  });
  /*

  afterAll(async () => {
    const createdUser = await userRepository.findByUsername('def');

    if (createdUser) {
      console.log('유저가 데이터베이스에 등록되었습니다:', createdUser);
    } else {
      console.error('유저 등록에 실패하였습니다.');
    }
  });
   */
});
