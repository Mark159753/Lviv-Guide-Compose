import { Field, Float, InputType, Int } from '@nestjs/graphql';
import { CreateLocationInput } from '../../location/dto/create-location.input';

@InputType()
export class CreatePlaceInput {
  @Field()
  title: string;

  @Field()
  description: string;

  @Field()
  headImage: string;

  @Field((type) => Float)
  rating: number;

  @Field()
  address: string;

  @Field(() => CreateLocationInput)
  location: CreateLocationInput;

  @Field((type) => Int)
  categoryId: number;
}
