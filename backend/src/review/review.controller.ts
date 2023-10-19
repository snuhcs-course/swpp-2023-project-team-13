import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  Req,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import { UserRequest } from '../core/user-request';
import { CreateReviewDto } from './dtos/in-dtos/createReview.dto';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApiBody, ApiTags } from '@nestjs/swagger';
import * as crypto from 'crypto';
import { S3ImageService } from '../core/s3Image.service';
import { ImageEntity } from './models/image.entity';
import { ImageDto } from './dtos/out-dtos/imageDto';
import { ReviewService } from './review.service';
import { JwtAccessGuard } from '../auth/guards';
import { ReviewRepository } from './repositories/review.repository';
import { ReviewListDto } from './dtos/out-dtos/reviewList.dto';

@ApiTags('reviews')
@Controller('reviews')
export class ReviewController {
  constructor(
    private s3ImageService: S3ImageService,
    private reviewService: ReviewService,
    private reviewRepository: ReviewRepository,
  ) {}

  @UseGuards(JwtAccessGuard)
  @Post('/')
  async createReview(
    @Req() { user }: UserRequest,
    @Body() data: CreateReviewDto,
  ) {
    await this.reviewService.create(user, data);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/restaurants/:restaurantId')
  async getReviewsOfRestaurant(
    @Req() { user }: UserRequest,
    @Param('restaurantId') restaurantId: string,
  ) {
    const reviews =
      await this.reviewRepository.findOfRestaurantId(restaurantId);

    return new ReviewListDto(reviews);
  }

  @UseGuards(JwtAccessGuard)
  @UseInterceptors(FileInterceptor('file'))
  @ApiBody({
    description: 'file 라는 이름으로 multipart/form-data 파일을 넣어주세요.',
  })
  async uploadImage(@UploadedFile() file: Express.Multer.File) {
    const extension = file?.originalname.split('.').pop() ?? 'jpg';

    const key = `${crypto.randomUUID()}.${extension}`;
    await this.s3ImageService.uploadImageFile(key, file);
    const cdnUrl = `${process.env.CDN_URL}${key}`;

    const image = await ImageEntity.create({
      url: cdnUrl,
    }).save();

    return new ImageDto(image);
  }
}
