import {
  Body,
  Controller,
  Get,
  Post,
  Req,
  Res,
  UnauthorizedException,
  UseGuards,
} from '@nestjs/common';
import { LocalAuthGuard } from './guards';
import { UserRequest } from '../core/user-request';
import { LoginDto } from './controller/in-dtos/login.dto';
import { TokenDto } from './controller/out-dtos/token.dto';
import { ApiTags } from '@nestjs/swagger';
import { AuthGuard } from '@nestjs/passport';
import { UserRepository } from '../user/repostiories/user.repository';
import { JwtPayload } from 'jsonwebtoken';
import { Response } from '@nestjs/common';
import { TokenGuard } from './token.guard';
import { Request } from '@nestjs/common';

@ApiTags('Auth')
@Controller('auth')
export class AuthController {
  constructor(private readonly userRepository: UserRepository) {}

  @UseGuards(LocalAuthGuard)
  @Post('login')
  async login(@Req() { user }: UserRequest, @Body() _: LoginDto) {
    const { accessToken, refreshToken } = user.createToken();
    return new TokenDto(accessToken, refreshToken);
  }

  //validate access token
  //
}
