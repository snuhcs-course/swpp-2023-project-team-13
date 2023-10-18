import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import * as process from 'process';
import { JwtPayload } from 'jsonwebtoken';
import { Injectable, UnauthorizedException } from '@nestjs/common';

@Injectable()
export class RefreshTokenStrategy extends PassportStrategy(
  Strategy,
  'jwt-refresh',
) {
  constructor() {
    super({
      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      secretOrKey: process.env.REFRESH_SECRET,
      ignoreExpiration: false,
    });
  }
  //
  // async validate(payload: JwtPayload) {
  //   const refreshToken = req.headers.authorization.split(' ')[1];
  //
  //   //const isValid =
  // }
}
