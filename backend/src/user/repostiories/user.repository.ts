import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { Repository } from 'typeorm';
import { UserEntity } from '../models/user.entity';
import { JwtPayload } from 'jsonwebtoken';
import { ConflictException, ForbiddenException } from '@nestjs/common';

@CustomRepository(UserEntity)
export class UserRepository extends Repository<UserEntity> {
  async findByUsername(username: string): Promise<UserEntity | undefined> {
    const user = await this.findOne({ where: { username } });
    return user ? user : undefined; // Return undefined when no user is found
  }

  async validateUniqueness(name: string, username: string) {
    const nameUser = await this.findOne({ where: { name } });
    const usernameUser = await this.findOne({ where: { username } });
    if (nameUser || usernameUser) {
      throw new ConflictException('이미 존재하는 이름 또는 아이디입니다.');
    }
  }

  async searchUserByUsernameSorted(name: string) {
    return await this.createQueryBuilder('user')
      .where('user.name ILIKE :name', { name: `%${name}%` })
      .getMany();
  }

  async getRandomFiveUsers() {
    return await this.createQueryBuilder('user')
      .orderBy('RANDOM()')
      .limit(5)
      .getMany();
  }
}
