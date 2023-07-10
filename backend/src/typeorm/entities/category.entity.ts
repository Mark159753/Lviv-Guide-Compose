import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { Field, Int, ObjectType } from '@nestjs/graphql';
import { PlaceEntity } from './place.entity';

@ObjectType()
@Entity({ name: 'categories' })
export class CategoryEntity {
  @Field((type) => Int)
  @PrimaryGeneratedColumn()
  id: number;

  @Field()
  @Column()
  name: string;

  @Field(() => [PlaceEntity])
  @OneToMany(() => PlaceEntity, (place) => place.category)
  places: PlaceEntity[];
}
