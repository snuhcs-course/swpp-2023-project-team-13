import { BeforeInsert, Column, Entity, BaseEntity } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import * as bcrypt from 'bcrypt';
import { sign } from 'jsonwebtoken';
import * as process from 'process';
import { TokenDto } from '../../auth/controller/out-dtos/token.dto';
import { JwtService } from '@nestjs/jwt';
import { UnauthorizedException } from '@nestjs/common';
import { UserRepository } from '../repostiories/user.repository';

@Entity()
export class UserEntity extends IssuedAtMetaEntity {
  @Column({ type: 'varchar' })
  name: string;
  @Column({ type: 'varchar' })
  username: string;
  @Column({ type: 'varchar' })
  password: string;

  private jwtService: JwtService;
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

  async refresh(refreshToken: string): Promise<TokenDto> {
    //verify token
    const decodedToken = this.jwtService.verify(refreshToken, {
      secret: process.env.REFRESH_SECRET,
    });

    const user = await this.userRepository.findOne(decodedToken.id);
    if (!user) {
      throw new UnauthorizedException('User not found');
    }

    //new access token
    const accessToken = this.jwtService.sign(
      { id: user.id, username: user.username },
      { secret: process.env.ACCESS_SECRET, expiresIn: '1d' },
    );

    return new TokenDto(accessToken, refreshToken);
  }
}
