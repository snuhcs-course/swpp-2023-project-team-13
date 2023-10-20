import { UserEntity } from '../../user/models/user.entity';

export type UserFixtureProps = {
  name: string;
  username: string;
  password: string;
};

export class UserFixture {
  static create({ name, username, password }: Partial<UserFixtureProps>) {
    return UserEntity.create({
      name: name ?? 'test',
      username: username ?? 'test',
      password: password ?? 'test',
    }).save();
  }
}
