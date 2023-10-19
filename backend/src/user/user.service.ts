import { Injectable, UnauthorizedException } from '@nestjs/common';
import { UserEntity } from './models/user.entity';
import { CreateUserDto } from './in-dtos/createuser.dto';
import { UserRepository } from './repostiories/user.repository';

@Injectable()
export class UserService {
  constructor(private userRepository: UserRepository) {}

  async create(createUserDto: CreateUserDto): Promise<UserEntity> {
    const { name, username, password } = createUserDto;
    const user = this.userRepository.create({ name, username, password });
    return this.userRepository.save(user);
  }

  async toggleFollow(user: UserEntity, followUser: UserEntity) {
    const follow = await user.findFollow(followUser);
    if (follow) {
      await user.unfollow(follow);
    } else {
      await user.follow(followUser);
    }
  }
}
