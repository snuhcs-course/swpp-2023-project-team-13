import { Controller, Post, Body, ConflictException } from '@nestjs/common';
import { UserService } from './user.service';
import { UserEntity } from './models/user.entity';
import { CreateUserDto } from './in-dtos/createuser.dto';
import { UserRepository } from './repostiories/user.repository';

@Controller('user')
export class UserController {
  constructor(
    private readonly userService: UserService,
    private readonly userRepository: UserRepository,
  ) {}

  @Post('/')
  async signUp(@Body() createUserDto: CreateUserDto) {
    const existingUser = await this.userRepository.findByUsername(
      createUserDto.username,
    );
    if (existingUser) {
      throw new ConflictException('Username already exists');
    }
    await this.userService.create(createUserDto);
  }
}
