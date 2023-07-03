import { Field, Float, Int, ObjectType } from '@nestjs/graphql';
import {
  Column,
  Entity,
  JoinColumn,
  OneToOne,
  PrimaryGeneratedColumn,
} from 'typeorm';
import { CategoryEntity } from './category.entity';
import { BaseEntity } from './base.entity';
import { LocationEntity } from './location.entity';

@ObjectType()
@Entity({ name: 'places' })
export class PlaceEntity extends BaseEntity {
  @Field((type) => Int)
  @PrimaryGeneratedColumn()
  id: number;

  @Field()
  @Column()
  title: string;

  @Field()
  @Column()
  description: string;

  @Field((type) => Int)
  @Column()
  categoryId: number;

  @Field((type) => Int)
  @Column()
  locationId: number;

  @Field()
  @Column()
  headImage: string;

  @Field((type) => Float)
  @Column()
  rating: number;

  @Field()
  @Column()
  address: string;

  @Field((type) => CategoryEntity)
  @JoinColumn({ name: 'categoryId' })
  @OneToOne(() => CategoryEntity)
  category: CategoryEntity;

  @Field((type) => LocationEntity)
  @JoinColumn({ name: 'locationId' })
  @OneToOne(() => LocationEntity)
  location: LocationEntity;
}
