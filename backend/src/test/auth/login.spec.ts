import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from '../../app.module';
import { Test } from '@nestjs/testing';
import { DataSource } from 'typeorm';
import { appSetting } from '../../main';
import * as supertest from 'supertest';
import { User } from '../../user/models/user.entity';
import { UserFixture } from '../fixture/user.fixture';
import { HttpStatus } from '@nestjs/common';
import { validateDtoKeys } from '../utils';

describe('login test', () => {
  let testServer: NestExpressApplication;
  let dataSource: DataSource;
  let user: User;

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

  it('맞지 않는 비밀번호', () => {
    supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        name: 'hi',
        username: 'hello',
        password: 'world2',
      })
      .expect(HttpStatus.UNAUTHORIZED);
  })
  it('존재하지 않는 유저로 찾기', async () => {
    await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        name: 'hi',
        username: 'asdfasdfasdf',
        password: 'world',
      })
      .expect(HttpStatus.UNAUTHORIZED);
  });

  it('login OK', async () => {
    await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        name: 'hi',
        username: 'hello',
        password: 'world',
      })
      .expect(HttpStatus.CREATED);
  });

  it('비밀번호 hash 로 적용되었는가?', () => {
    expect(user.password).not.toEqual('world');
  });

  it('Response body 형식 체크', async () => {
    const { body } = await supertest(testServer.getHttpServer())
      .post('/auth/login')
      .send({
        username: 'hello',
        password: 'world',
      })
      .expect(HttpStatus.CREATED);

    validateDtoKeys(body, ['accessToken', 'refreshToken']);
    expect(body.accessToken).not.toEqual(body.refreshToken);
  });
});
