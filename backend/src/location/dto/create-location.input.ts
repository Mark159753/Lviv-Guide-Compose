import { InputType, Field, Float } from '@nestjs/graphql';

@InputType()
export class CreateLocationInput {
  @Field((type) => Float)
  lat: number;
  @Field((type) => Float)
  lon: number;
}
