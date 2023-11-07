import { Column, Entity, OneToMany } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import { ReviewEntity } from './review.entity';

@Entity()
export class RestaurantEntity extends IssuedAtMetaEntity {
  @Column({ type: 'varchar' })
  googleMapPlaceId: string;

  @Column({ type: 'float8' })
  latitude: number;

  @Column({ type: 'float8' })
  longitude: number;

  @Column({ type: 'varchar', default: '' })
  name: string;

  @OneToMany(() => ReviewEntity, (review) => review.restaurant)
  reviews: ReviewEntity[];
}
