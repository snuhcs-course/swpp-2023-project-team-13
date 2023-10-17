import { HttpException, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { JwtPayload } from 'jsonwebtoken';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { AuthService } from '../auth.service';
import { JwtService } from '@nestjs/jwt';

export class AccessTokenStrategy extends PassportStrategy(Strategy, 'jwt') {
  constructor(
    private authService: AuthService,
    private jwtService: JwtService,
  ) {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      secretOrKey: process.env.ACCESS_SECRET,
    });
  }

  public validateToken(token: string): any {
    const secretKey = process.env.ACCESS_SECRET;

    try {
      const verified = this.jwtService.verify(token, { secret: secretKey });
      return verified;
    } catch (e: any) {
      if (e.message === 'invalid signature') {
        throw new HttpException('Invalid token', 401);
      } else if (e.message === 'jwt expired') {
        throw new HttpException('Token expired', 410);
      } else if (e.message === 'jwt malformed') {
        throw new HttpException('Malformed token', 400);
      } else {
        throw new HttpException('Internal server error', 500);
      }
    }
  }
}
