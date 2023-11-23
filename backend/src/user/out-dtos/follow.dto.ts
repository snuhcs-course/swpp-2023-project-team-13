import { UserListDto } from './userList.dto';
import { UserSummaryDto } from './userSummary.dto';
import { UserEntity } from '../models/user.entity';

export class FollowDto {
  private followers: UserSummaryDto[];
  private followings: UserSummaryDto[];

  constructor(followers: UserEntity[], followings: UserEntity[]) {
    this.followers = followers.map((user) => new UserSummaryDto(user));
    this.followings = followings.map((user) => new UserSummaryDto(user));
  }
}
