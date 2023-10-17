import { AuthGuard } from '@nestjs/passport';
import {
  ExecutionContext,
  HttpException,
  HttpStatus,
  Injectable,
} from '@nestjs/common';
import { JwtService } from '@nestjs/jwt';
import { AccessTokenStrategy } from './strategies/access.token.strategy';

@Injectable()
export class TokenGuard extends AuthGuard('jwt') {
  constructor(private accessTokenStrategy: AccessTokenStrategy) {
    super();
  }

  async canActivate(context: ExecutionContext) {
    const request = context.switchToHttp().getRequest();
    const { authorization } = request.headers;

    if (authorization === undefined) {
      throw new HttpException('Token not provided', HttpStatus.UNAUTHORIZED);
    }

    const token = authorization.split(' ')[1];
    return await this.accessTokenStrategy.validateToken(token);
  }
}
