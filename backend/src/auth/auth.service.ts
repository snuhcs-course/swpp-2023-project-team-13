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
}
