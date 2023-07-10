import { ArgsType, Field, Int } from '@nestjs/graphql';

@ArgsType()
export class FetchPlacesArgs {
  @Field(() => Int, { nullable: true })
  page?: number = null;
  @Field(() => Int, { nullable: true })
  size?: number = null;
  @Field(() => Int, { nullable: true })
  categoryId?: number = null;
}
