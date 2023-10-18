import { IsArray, IsNumber, IsString, ValidateNested } from 'class-validator';
import { RestaurantDto } from './restaurant.dto';

export class CreateReviewDto {
  @IsString()
  content: string;

  @IsArray()
  @IsNumber({}, { each: true })
  imageIds: number[];

  @IsNumber()
  receiptImageId: number;

  @ValidateNested()
  restaurant: RestaurantDto;
}
