import { UserEntity } from '../models/user.entity';

export class UserSummaryDto {
  private id: number;
  private name: string;

  constructor({ id, name }: UserEntity) {
    this.id = id;
    this.name = name;
  }
}
