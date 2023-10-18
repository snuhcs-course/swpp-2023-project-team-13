import { ImageEntity } from '../../models/image.entity';

export class ImageDto {
  private id: number;
  private url: string;
  private isReceipt: boolean;
  private isReceiptVerified: boolean;

  constructor({ id, url, isReceipt, isReceiptVerified }: ImageEntity) {
    this.id = id;
    this.url = url;
    this.isReceipt = isReceipt;
    this.isReceiptVerified = isReceiptVerified;
  }
}
