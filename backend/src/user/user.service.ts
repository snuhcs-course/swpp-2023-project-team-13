import { Injectable, UnauthorizedException } from '@nestjs/common';
import { UserEntity } from './models/user.entity';
import { CreateUserDto } from './in-dtos/createuser.dto';
import { UserRepository } from './repostiories/user.repository';
import { TokenDto } from '../auth/controller/out-dtos/token.dto';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class UserService {
  constructor(
    private userRepository: UserRepository,
    private jwtService: JwtService,
  ) {}

  async create(createUserDto: CreateUserDto): Promise<UserEntity> {
    const { name, username, password } = createUserDto;
    const user = this.userRepository.create({ name, username, password });
    return this.userRepository.save(user);
  }
}
