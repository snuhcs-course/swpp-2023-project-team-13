import { Column, Entity, ManyToOne, OneToMany } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import { RestaurantEntity } from './restaurant.entity';
import { ImageEntity } from './image.entity';
import { UserEntity } from '../../user/models/user.entity';

@Entity()
export class ReviewEntity extends IssuedAtMetaEntity {
  @ManyToOne(() => RestaurantEntity, (restaurant) => restaurant.reviews)
  restaurant: RestaurantEntity;

  @ManyToOne(() => UserEntity, (user) => user.reviews)
  user: UserEntity;

  @OneToMany(() => ImageEntity, (image) => image.review)
  images: ImageEntity[];

  @Column({ type: 'varchar' })
  content: string;

  @Column({ type: 'boolean', default: false })
  isPositive: boolean;

  get receiptImage() {
    return this.images.find((image) => image.isReceipt);
  }
}
