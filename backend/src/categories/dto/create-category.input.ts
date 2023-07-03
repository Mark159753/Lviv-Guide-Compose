import { InputType, Int, Field } from '@nestjs/graphql';
import { IsAlpha, IsNotEmpty } from 'class-validator';

@InputType()
export class CreateCategoryInput {
  @IsNotEmpty()
  @IsAlpha()
  @Field()
  name: string;
}
