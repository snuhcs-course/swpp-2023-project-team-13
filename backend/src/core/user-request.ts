import { User } from '../user/models/user.entity';

export interface UserRequest extends Request {
  user: User;
}
