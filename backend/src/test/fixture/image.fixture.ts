import { ReviewEntity } from '../../review/models/review.entity';
import { ImageEntity } from '../../review/models/image.entity';

type ImageFixtureProps = {
  url: string;
  isReceipt: boolean;
  isReceiptVerified: boolean;
};

export class ImageFixture {
  static create({
    url,
    isReceipt,
    isReceiptVerified,
  }: Partial<ImageFixtureProps>) {
    return ImageEntity.create({
      url: url ?? 'https://test.com',
      isReceipt: isReceipt ?? false,
      isReceiptVerified: isReceiptVerified ?? false,
    }).save();
  }
}
