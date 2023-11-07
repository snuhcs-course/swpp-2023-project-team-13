import {
  Controller,
  Post,
  Body,
  ConflictException,
  Patch,
  UseGuards,
  Req,
  ForbiddenException,
  Put,
  Param,
  NotFoundException,
  Get,
} from '@nestjs/common';
import { UserService } from './user.service';
import { UserEntity } from './models/user.entity';
import { CreateUserDto } from './in-dtos/createuser.dto';
import { UserRepository } from './repostiories/user.repository';
import { JwtAccessGuard } from '../auth/guards';
import { UserRequest } from '../core/user-request';
import { ChangePasswordDto } from './in-dtos/changePasswordDto';
import { ApiOperation } from '@nestjs/swagger';
import { UserInfoDto } from './out-dtos/userInfo.dto';
import { UserListDto } from './out-dtos/userList.dto';
import stringSimilarity from 'string-similarity-js';

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

  @UseGuards(JwtAccessGuard)
  @Patch('/me/password')
  async updatePassword(
    @Req() { user }: UserRequest,
    @Body() { currentPassword, newPassword }: ChangePasswordDto,
  ) {
    if (!user.checkPasswordMatches(currentPassword)) {
      throw new ForbiddenException('패스워드가 일치하지 않습니다.');
    }

    await user.updatePassword(newPassword);
  }

  @UseGuards(JwtAccessGuard)
  @ApiOperation({
    description:
      '유저 팔로우를 토글합니다. 팔로우가 되어있으면 언팔로우, 안되어있으면 팔로우합니다.',
  })
  @Put('/follow/:userId')
  async toggleFollow(
    @Req() { user }: UserRequest,
    @Param('userId') userId: number,
  ) {
    if (user.id === userId) {
      throw new ForbiddenException('자기 자신을 팔로우할 수 없습니다.');
    }
    const followUser = await UserEntity.findOneBy({
      id: userId,
    });

    if (!followUser) {
      throw new NotFoundException('팔로우할 유저가 존재하지 않습니다.');
    }

    await this.userService.toggleFollow(user, followUser);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/me')
  async getMyInfo(@Req() { user }: UserRequest) {
    const followerCount = await user.getFollowerCount();
    const followingCount = await user.getFollowingCount();

    return new UserInfoDto(user, followerCount, followingCount);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/search/:name')
  async search(@Param('name') name: string) {
    let users = await this.userRepository.searchUserByUsernameSorted(name);

    users.sort(
      (a, b) =>
        -stringSimilarity(a.name, name) + stringSimilarity(b.name, name),
    );

    if (users.length === 0) {
      users = await this.userRepository.getRandomFiveUsers();
    }

    return new UserListDto(users);
  }
}
