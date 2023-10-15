import { UserEntity } from '../user/models/user.entity';

export interface UserRequest extends Request {
  user: UserEntity;
}
