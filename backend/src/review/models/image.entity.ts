import { Column, Entity, ManyToOne } from 'typeorm';
import { IssuedAtMetaEntity } from '../../core/models/base.entity';
import { ReviewEntity } from './review.entity';

@Entity()
export class ImageEntity extends IssuedAtMetaEntity {
  @Column({ type: 'varchar' })
  url: string;

  @Column({ type: 'boolean', default: false })
  isReceipt: boolean;

  @Column({ type: 'boolean', default: false })
  isReceiptVerified: boolean;

  @ManyToOne(() => ReviewEntity, (review) => review.images)
  review: ReviewEntity;

  async markAsReceipt() {
    this.isReceipt = true;
    await this.save();
  }

  async markAsReceiptVerified() {
    this.isReceiptVerified = true;
    await this.save();
  }
}
