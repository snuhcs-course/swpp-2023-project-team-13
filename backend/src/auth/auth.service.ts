import { CreateUserDto } from '../user/in-dtos/createuser.dto';
import { UserEntity } from '../user/models/user.entity';
import { UserRepository } from '../user/repostiories/user.repository';
import { Injectable, UnauthorizedException } from '@nestjs/common';
import { JwtPayload } from 'jsonwebtoken';

@Injectable()
export class AuthService {
  constructor(private userRepository: UserRepository) {}

  async validateUserByToken(payload: JwtPayload) {
    const { username } = payload;
    const user = await this.userRepository.findByUsername(username);
    if (!user) {
      return null;
    }
    return user;
  }
}
