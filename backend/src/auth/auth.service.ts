import { CreateUserDto } from '../user/in-dtos/createuser.dto';
import { JwtService } from '@nestjs/jwt';
import { UserEntity } from '../user/models/user.entity';
import { UserRepository } from '../user/repostiories/user.repository';
import { UnauthorizedException } from '@nestjs/common';
import { JwtPayload } from 'jsonwebtoken';

export class AuthService {
  constructor(
    private userRepository: UserRepository,
    private jwtService: JwtService,
  ) {}

  async validateUserByToken(payload: JwtPayload) {
    const { username } = payload;
    const user = await this.userRepository.findByUsername(username);
    if (!user) {
      return null;
    }
    return user;
  }
}
