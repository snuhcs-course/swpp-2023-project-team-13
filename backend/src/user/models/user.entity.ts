import { BeforeInsert, Column, Entity, BaseEntity, OneToMany } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import * as bcrypt from 'bcrypt';
import { JwtPayload, sign } from 'jsonwebtoken';
import * as process from 'process';
import { TokenDto } from '../../auth/controller/out-dtos/token.dto';
import { UnauthorizedException } from '@nestjs/common';
import { UserRepository } from '../repostiories/user.repository';
import { ReviewEntity } from '../../review/models/review.entity';

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

  checkPasswordMatches(password: string): boolean {
    return bcrypt.compareSync(password, this.password);
  }

  @BeforeInsert()
  async hashPassword() {
    this.password = await bcrypt.hash(this.password, 10);
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
}
