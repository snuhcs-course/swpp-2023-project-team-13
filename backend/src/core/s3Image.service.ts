import { BadRequestException, Injectable } from '@nestjs/common';
import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3';

@Injectable()
export class S3ImageService {
  async uploadImageFile(key: string, file: Express.Multer.File | undefined) {
    if (!file) throw new BadRequestException('Bad file upload');

    return this.uploadImageBuffer(key, file.buffer);
  }

  async uploadImageBuffer(key: string, buffer: Buffer) {
    if (process.env.NODE_ENV === 'test') return;

    const client = new S3Client({
      region: 'ap-northeast-2',
      credentials: {
        accessKeyId: process.env.AWS_ACCESS_KEY_ID!,
        secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY!,
      },
    });

    try {
      await client.send(
        new PutObjectCommand({
          Key: key,
          Body: buffer,
          Bucket: process.env.S3_BUCKET_NAME!,
        }),
      );
    } catch (error) {
      throw new BadRequestException('Bad file upload');
    }
  }
}
