import { IsNumber } from 'class-validator';
import { Transform, Type } from 'class-transformer';

export class ReviewAdjacentQueryDto {
  @IsNumber()
  @Type(() => Number)
  latitude: number;

  @IsNumber()
  @Type(() => Number)
  longitude: number;

  @IsNumber()
  @Type(() => Number)
  distance: number;
}
