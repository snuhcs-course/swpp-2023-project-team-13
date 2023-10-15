import { Injectable, UnauthorizedException } from '@nestjs/common';
import { Strategy } from 'passport-local';
import { PassportStrategy } from '@nestjs/passport';
import { UserEntity } from '../../user/models/user.entity';

@Injectable()
export class LocalStrategy extends PassportStrategy(Strategy) {
  constructor() {
    super();
  }

  async validate(username: string, password: string): Promise<any> {
    const user = await UserEntity.findOne({
      where: {
        username,
      },
    });

    if (user && user.checkPasswordMatches(password)) {
      return user;
    } else throw new UnauthorizedException();
  }
}
