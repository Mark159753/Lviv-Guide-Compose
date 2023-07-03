import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';
import { Field, Float, Int, ObjectType } from '@nestjs/graphql';

@ObjectType()
@Entity({ name: 'locations' })
export class LocationEntity {
  @Field((type) => Int)
  @PrimaryGeneratedColumn()
  id: number;

  @Field((type) => Float)
  @Column('decimal')
  lat: number;

  @Field((type) => Float)
  @Column('decimal')
  lon: number;
}
