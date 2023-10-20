import { Entity, ManyToOne } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import { UserEntity } from './user.entity';

@Entity()
export class FollowEntity extends IssuedAtMetaEntity {
  @ManyToOne(() => UserEntity)
  user: UserEntity;

  @ManyToOne(() => UserEntity, (user) => user.followers)
  follower: UserEntity;
}
