import { BaseEntity } from './base.entity';
import {
  Column,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { Field, Int, ObjectType } from '@nestjs/graphql';
import { PlaceEntity } from './place.entity';

@ObjectType()
@Entity({ name: 'images' })
export class ImageEntity extends BaseEntity {
  @Field((type) => Int)
  @PrimaryGeneratedColumn()
  id: number;

  @Field()
  @Column()
  path: string;

  @Field(() => PlaceEntity)
  @JoinColumn({ name: 'placeId' })
  @ManyToOne(() => PlaceEntity, (place) => place.images, {
    onDelete: 'CASCADE',
  })
  place: PlaceEntity;
}
