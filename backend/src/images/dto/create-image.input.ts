import { InputType, Int, Field } from '@nestjs/graphql';

@InputType()
export class CreateImageInput {
  @Field()
  path: string;
}
