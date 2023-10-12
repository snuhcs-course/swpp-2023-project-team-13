import { User } from '../../user/models/user.entity';

export type UserFixtureProps = {
  username: string;
  password: string;
};

export class UserFixture {
  static create({ username, password }: Partial<UserFixtureProps>) {
    return User.create({
      username: username ?? 'test',
      password: password ?? 'test',
    }).save();
  }
}
