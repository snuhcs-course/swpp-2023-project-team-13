import { ImageEntity } from '../../models/image.entity';

export class ImageUploadDto {
  private id: number;
  private url: string;
  private isReceiptVerified: boolean;

  constructor({ id, url, isReceipt, isReceiptVerified }: ImageEntity) {
    this.id = id;
    this.url = url;
    this.isReceiptVerified = isReceiptVerified;
  }
}
