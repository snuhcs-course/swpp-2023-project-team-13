import { Body, Controller, Post, Req, UseGuards } from '@nestjs/common';
import { LocalAuthGuard } from './guards';
import { UserRequest } from '../core/user-request';
import { LoginDto } from './controller/in-dtos/login.dto';
import { TokenDto } from './controller/out-dtos/token.dto';
import { ApiTags } from '@nestjs/swagger';
import { UserRepository } from '../user/repostiories/user.repository';

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
}
