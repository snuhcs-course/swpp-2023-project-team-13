import { Module } from '@nestjs/common';
import { S3ImageService } from './s3Image.service';

@Module({
  providers: [S3ImageService],
  controllers: [],
  exports: [S3ImageService],
})
export class CoreModule {}
