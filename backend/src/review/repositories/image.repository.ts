import { CustomRepository } from '../../typeorm-ex/typeorm-ex.decorator';
import { ImageEntity } from '../models/image.entity';
import { Repository } from 'typeorm';

@CustomRepository(ImageEntity)
export class ImageRepository extends Repository<ImageEntity> {}
