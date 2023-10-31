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
import { validateDtoKeys } from '../utils';
import { decode } from 'jsonwebtoken';

describe('getUserMe test', () => {
  let testServer: NestExpressApplication;
  let user: UserEntity;

  beforeAll(async () => {
    const module = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();
  });

  beforeEach(async () => {
    user = UserEntity.create({
      id: 8,
      username: 'hello',
      password: 'world',
      name: 'hi',
    });
  });

  it('token creation', async () => {
    const token = user.createToken();
    expect(token).toHaveProperty('accessToken');
    expect(token).toHaveProperty('refreshToken');
  });

  it('token payload', async () => {
    const payload = user.tokenPayload;

    expect(payload).toHaveProperty('id');
    expect(payload).toHaveProperty('username');
    expect(payload).toHaveProperty('iat');
  });

  it('decode access token', () => {
    const { accessToken } = user.createToken();
    const payload = decode(accessToken);

    expect(payload).toHaveProperty('id');
    expect(payload).toHaveProperty('username');
    expect(payload).toHaveProperty('iat');
  });
});
