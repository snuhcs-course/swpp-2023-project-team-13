import { Module } from '@nestjs/common';
import { AuthController } from './auth.controller';
import { LocalStrategy } from './strategies/local.strategy';
import { AccessTokenStrategy } from './strategies/access.token.strategy';
import { RefreshTokenStrategy } from './strategies/refresh.token.strategy';
import { AuthService } from './auth.service';
import { UserRepository } from '../user/repostiories/user.repository';
import { TypeOrmExModule } from '../typeorm-ex/typeorm-ex.module';

@Module({
  imports: [TypeOrmExModule.forCustomRepository([UserRepository])],
  controllers: [AuthController],
  providers: [
    AuthService,
    LocalStrategy,
    AccessTokenStrategy,
    RefreshTokenStrategy,
  ],
})
export class AuthModule {}
