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
import { ReviewEntity } from '../../review/models/review.entity';

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
    await supertest(testServer.getHttpServer()).post('/user').send({
      name: 'John Doe',
      username: 'hello',
      password: 'password123',
    });
    expect(HttpStatus.CONFLICT);

    expect(await UserEntity.count()).toBe(1);
  });

  it('Check if the name field is not empty', async () => {
    const response = await supertest(testServer.getHttpServer())
      .post('/user')
      .send({
        name: '',
        username: 'def',
        password: 'ghi',
      })
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('Check if the username field is not empty', async () => {
    const response = await supertest(testServer.getHttpServer())
      .post('/user')
      .send({
        name: 'abc',
        username: '',
        password: 'ghi',
      })
      .expect(HttpStatus.BAD_REQUEST);
  });

  it('Check if the password field is not empty', async () => {
    const response = await supertest(testServer.getHttpServer())
      .post('/user')
      .send({
        name: 'abc',
        username: 'def',
        password: '',
      })
      .expect(HttpStatus.BAD_REQUEST);
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

    const userEntity = await UserEntity.findOneOrFail({
      where: {
        username: 'def',
      },
    });
    expect(userEntity.name).toEqual('abc');
    expect(userEntity.username).toEqual('def');
  });
});
