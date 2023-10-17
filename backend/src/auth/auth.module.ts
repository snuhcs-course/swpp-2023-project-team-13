import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { LocalStrategy } from './strategies/local.strategy';
import { AccessTokenStrategy } from './strategies/access.token.strategy';
import { RefreshTokenStrategy } from './strategies/refresh.token.strategy';
import { AuthService } from './auth.service';
import { UserRepository } from '../user/repostiories/user.repository';

@Module({
  controllers: [AuthController],
  providers: [
    LocalStrategy,
    AccessTokenStrategy,
    RefreshTokenStrategy,
    UserRepository,
    AuthService,
  ],
})
export class AuthModule {}
