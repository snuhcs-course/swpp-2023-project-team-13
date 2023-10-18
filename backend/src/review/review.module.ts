import { Module } from '@nestjs/common';
import { ReviewController } from './review.controller';
import { ReviewService } from './review.service';
import { TypeOrmExModule } from '../typeorm-ex/typeorm-ex.module';
import { RestaurantRepository } from './repositories/restaurant.repository';
import { ImageRepository } from './repositories/image.repository';
import { CoreModule } from '../core/core.module';

@Module({
  imports: [
    TypeOrmExModule.forCustomRepository([
      RestaurantRepository,
      ImageRepository,
    ]),
    CoreModule,
  ],
  controllers: [ReviewController],
  providers: [ReviewService],
})
export class ReviewModule {}
