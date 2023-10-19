import { BeforeInsert, Column, Entity, OneToMany } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import * as bcrypt from 'bcrypt';
import { sign } from 'jsonwebtoken';
import * as process from 'process';
import { ForbiddenException } from '@nestjs/common';
import { UserRepository } from '../repostiories/user.repository';
import { ReviewEntity } from '../../review/models/review.entity';
import { FollowEntity } from './follow.entity';

@Entity()
export class UserEntity extends IssuedAtMetaEntity {
  @Column({ type: 'varchar' })
  name: string;
  @Column({ type: 'varchar' })
  username: string;
  @Column({ type: 'varchar' })
  password: string;
  @OneToMany(() => ReviewEntity, (review) => review.user)
  reviews: ReviewEntity[];

  private userRepository: UserRepository;

  @OneToMany(() => FollowEntity, (follow) => follow.user)
  followers: FollowEntity[];

  @OneToMany(() => FollowEntity, (follow) => follow.follower)
  followings: FollowEntity[];

  checkPasswordMatches(password: string): boolean {
    return bcrypt.compareSync(password, this.password);
  }

  @BeforeInsert()
  async hashPasswordOnInsert() {
    this.hashPassword(this.password);
  }

  createToken() {
    const accessToken = this.createAccessToken();
    const refreshToken = this.createRefreshToken();

    return {
      accessToken,
      refreshToken,
    };
  }

  public createAccessToken() {
    return sign(this.tokenPayload, process.env.ACCESS_SECRET!, {
      expiresIn: '1d',
    });
  }

  private createRefreshToken() {
    return sign(this.tokenPayload, process.env.REFRESH_SECRET!, {
      expiresIn: '30d',
    });
  }

  get tokenPayload() {
    return {
      id: this.id,
      username: this.username,
    };
  }

  async updatePassword(newPassword: string) {
    this.hashPassword(newPassword);
    await this.save();
  }

  private hashPassword(password: string) {
    this.password = bcrypt.hashSync(password, 10);
  }

  async follow(user: UserEntity) {
    return FollowEntity.create({
      user: this,
      follower: user,
    }).save();
  }

  async unfollow(follow: FollowEntity) {
    await follow.softRemove();
  }

  async findFollow(followUser: UserEntity) {
    return await FollowEntity.findOne({
      where: {
        user: { id: this.id },
        follower: { id: followUser.id },
      },
    });
  }

  getFollowerCount() {
    return FollowEntity.countBy({
      follower: { id: this.id },
    });
  }

  getFollowingCount() {
    return FollowEntity.countBy({
      user: { id: this.id },
    });
  }
}
