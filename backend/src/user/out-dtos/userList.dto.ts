import { UserEntity } from '../models/user.entity';
import { UserSummaryDto } from './userSummary.dto';

export class UserListDto {
  private userList: UserSummaryDto[];

  constructor(userList: UserEntity[]) {
    this.userList = userList.map((user) => new UserSummaryDto(user));
  }
}
