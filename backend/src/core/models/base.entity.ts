import {
  BaseEntity,
  CreateDateColumn,
  DeleteDateColumn,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { ApiHideProperty } from '@nestjs/swagger';
import { Exclude } from 'class-transformer';
import { DateTime } from 'luxon';

export abstract class IssuedAtMetaEntity extends BaseEntity {
  @CreateDateColumn({
    type: 'timestamptz',
    nullable: false,
  })
  @Exclude()
  @ApiHideProperty()
  issuedAt!: Date;

  @PrimaryGeneratedColumn()
  id!: number;

  @DeleteDateColumn()
  @Exclude()
  @ApiHideProperty()
  deletedAt: Date | null = null;

  getIssuedAt(): DateTime {
    const date = new Date(this.issuedAt);
    return DateTime.fromJSDate(date);
  }
}
