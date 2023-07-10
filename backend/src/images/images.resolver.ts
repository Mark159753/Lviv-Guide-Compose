import { Resolver, Query, Mutation, Args, Int } from '@nestjs/graphql';
import { ImagesService } from './images.service';
import { CreateImageInput } from './dto/create-image.input';
import { ImageEntity } from '../typeorm/entities/image.entity';

@Resolver(() => ImageEntity)
export class ImagesResolver {
  constructor(private readonly imagesService: ImagesService) {}

  @Mutation(() => ImageEntity)
  createImage(@Args('createImageInput') createImageInput: CreateImageInput) {
    return this.imagesService.create(createImageInput);
  }

  @Query(() => [ImageEntity], { name: 'imagesByPlace' })
  findByPlace(@Args('placeId', { type: () => Int }) placeId: number) {
    return this.imagesService.findByPlace(placeId);
  }

  @Query(() => ImageEntity, { name: 'image' })
  findOne(@Args('id', { type: () => Int }) id: number) {
    return this.imagesService.findOne(id);
  }

  @Mutation(() => ImageEntity)
  removeImage(@Args('id', { type: () => Int }) id: number) {
    return this.imagesService.remove(id);
  }
}
