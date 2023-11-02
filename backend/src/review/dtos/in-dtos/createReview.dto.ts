import {
  IsArray,
  IsNumber,
  IsOptional,
  IsString,
  ValidateNested,
} from 'class-validator';
import { RestaurantDto } from './restaurant.dto';
import { Type } from 'class-transformer';

export class CreateReviewDto {
  @IsString()
  content: string;

  @IsArray()
  @IsNumber({}, { each: true })
  imageIds: number[];

  @IsNumber()
  @IsOptional()
  receiptImageId?: number;

  @ValidateNested()
  @Type(() => RestaurantDto)
  restaurant: RestaurantDto;
}
