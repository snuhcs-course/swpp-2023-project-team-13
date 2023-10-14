import { BeforeInsert, Column, Entity, BaseEntity } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import * as bcrypt from 'bcrypt';
import { sign } from 'jsonwebtoken';
import * as process from 'process';

@Entity()
export class User extends IssuedAtMetaEntity {
  @Column({ type: 'varchar' })
  name: string;
  @Column({ type: 'varchar' })
  username: string;
  @Column({ type: 'varchar' })
  password: string;

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

  private createAccessToken() {
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
