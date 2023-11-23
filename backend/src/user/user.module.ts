import { Module } from '@nestjs/common';
import { UserController } from './user.controller';
import { UserService } from './user.service';
import { TypeOrmExModule } from '../typeorm-ex/typeorm-ex.module';
import { UserRepository } from './repostiories/user.repository';
import { JwtModule } from '@nestjs/jwt';
import { CustomQueryCommand } from './custom-query.command';

@Module({
  imports: [TypeOrmExModule.forCustomRepository([UserRepository]), JwtModule],
  controllers: [UserController],
  providers: [UserService, CustomQueryCommand],
})
export class UserModule {}
