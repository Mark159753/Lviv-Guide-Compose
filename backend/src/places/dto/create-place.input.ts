import { Field, Float, InputType, Int } from '@nestjs/graphql';
import { CreateLocationInput } from '../../location/dto/create-location.input';
import { GraphQLUpload, FileUpload } from 'graphql-upload';

@InputType()
export class CreatePlaceInput {
  @Field()
  title: string;

  @Field()
  description: string;

  @Field(() => GraphQLUpload)
  headImage: Promise<FileUpload>;

  @Field((type) => Float)
  rating: number;

  @Field()
  address: string;

  @Field(() => CreateLocationInput)
  location: CreateLocationInput;

  @Field((type) => Int)
  categoryId: number;

  @Field(() => [GraphQLUpload])
  images: Promise<FileUpload>[];
}
