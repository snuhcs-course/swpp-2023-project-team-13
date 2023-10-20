import { ReviewDetailDto } from './reviewDetail.dto';
import { ReviewEntity } from '../../models/review.entity';

export class ReviewListDto {
  private reviewList: ReviewDetailDto[];

  constructor(reviewList: ReviewEntity[]) {
    this.reviewList = reviewList.map(
      (review) => new ReviewDetailDto(review, review.user),
    );
  }
}
