import {
  Body,
  Controller,
  Get,
  NotFoundException,
  Param,
  Post,
  Query,
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
import { ImageUploadDto } from './dtos/out-dtos/imageUpload.dto';
import { ReviewDetailDto } from './dtos/out-dtos/reviewDetail.dto';
import { UserEntity } from '../user/models/user.entity';
import { UserRepository } from '../user/repostiories/user.repository';
import { ReviewAdjacentQueryDto } from './dtos/in-dtos/review-adjacent-query.dto';
import { RestaurantListDto } from './dtos/out-dtos/restaurantList.dto';
import { RestaurantRepository } from './repositories/restaurant.repository';

@ApiTags('reviews')
@Controller('reviews')
export class ReviewController {
  constructor(
    private s3ImageService: S3ImageService,
    private reviewService: ReviewService,
    private reviewRepository: ReviewRepository,
    private restaurantRepository: RestaurantRepository,
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
  @Get('/my')
  async getMyReview(@Req() { user }: UserRequest) {
    const reviews = await this.reviewRepository.findOfUser(user);
    return new ReviewListDto(reviews);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/random')
  async getReviewRandom() {
    const limit = 5; // Define the number of random reviews you want to retrieve
    const randomReviews = await this.reviewRepository.findRandomReviews(limit);
    return new ReviewListDto(randomReviews);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/adjacent/restaurants')
  async getReviewAdjacent(
    @Req() { user }: UserRequest,
    @Query() data: ReviewAdjacentQueryDto,
  ) {
    const allRestaurants = await this.restaurantRepository.find({});

    const restaurants = this.reviewService.getAdjacentRestaurant(
      data,
      allRestaurants,
    );

    return new RestaurantListDto(restaurants);
  }

  @UseGuards(JwtAccessGuard)
  @Get('/:reviewId')
  async getReviewDetail(
    @Req() { user }: UserRequest,
    @Param('reviewId') reviewId: number,
  ) {
    const review = await this.reviewRepository.findOfReviewId(reviewId);

    if (!review) throw new NotFoundException('리뷰를 찾을 수 없습니다.');
    return new ReviewDetailDto(review, review.user);
  }

  @UseGuards(JwtAccessGuard)
  @UseInterceptors(FileInterceptor('file'))
  @ApiBody({
    description: 'file 라는 이름으로 multipart/form-data 파일을 넣어주세요.',
  })
  @Post('/images')
  async uploadImage(@UploadedFile() file: Express.Multer.File) {
    const extension = file?.originalname.split('.').pop() ?? 'jpg';

    const key = `${crypto.randomUUID()}.${extension}`;
    await this.s3ImageService.uploadImageFile(key, file);
    const cdnUrl = `${process.env.CDN_URL}${key}`;

    const image = await ImageEntity.create({
      url: cdnUrl,
    }).save();

    return new ImageUploadDto(image);
  }
}
