import { UserEntity } from '../models/user.entity';

export class UserInfoDto {
  private id: number;
  private profileImage: string;
  private name: string;
  private username: string;
  private followerCount: number;
  private followingCount: number;

  constructor(
    { id, name, username }: UserEntity,
    followerCount: number,
    followingCount: number,
  ) {
    this.id = id;
    this.profileImage = `${process.env.CDN_URL!}profile${id % 10}.jpg`;
    this.name = name;
    this.username = username;
    this.followerCount = followerCount;
    this.followingCount = followingCount;
  }
}
