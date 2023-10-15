import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { Repository } from 'typeorm';
import { UserEntity } from '../models/user.entity';

@CustomRepository(UserEntity)
export class UserRepository extends Repository<UserEntity> {
  async findByUsername(username: string): Promise<UserEntity | undefined> {
    const user = await this.findOne({ where: { username } });
    return user ? user : undefined; // Return undefined when no user is found
  }
}
