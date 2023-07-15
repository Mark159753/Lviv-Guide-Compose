import {
  Args,
  Int,
  Mutation,
  Parent,
  Query,
  ResolveField,
  Resolver,
} from '@nestjs/graphql';
import { PlacesService } from './places.service';
import { PlaceEntity } from '../typeorm/entities/place.entity';
import { CreatePlaceInput } from './dto/create-place.input';
import { LocationService } from '../location/location.service';
import { LocationEntity } from '../typeorm/entities/location.entity';
import { CategoryEntity } from '../typeorm/entities/category.entity';
import { CategoriesService } from '../categories/categories.service';
import { ImageEntity } from '../typeorm/entities/image.entity';
import { ImagesService } from '../images/images.service';
import { FetchPlacesArgs } from "./dto/fetch-places.args";

@Resolver((of) => PlaceEntity)
export class PlacesResolver {
  constructor(
    private readonly placesService: PlacesService,
    private readonly locationService: LocationService,
    private readonly categoriesService: CategoriesService,
    private readonly imagesService: ImagesService,
  ) {}

  @Mutation(() => PlaceEntity)
  async createPlace(
    @Args('createPlaceInput') createPlaceInput: CreatePlaceInput,
  ) {
    return this.placesService.create(createPlaceInput);
  }

  @Query((returns) => [PlaceEntity], { name: 'places' })
  findAll(@Args() args: FetchPlacesArgs): Promise<PlaceEntity[]> {
    return this.placesService.findAll(args);
  }

  @Query(() => PlaceEntity, { name: 'place' })
  findOne(@Args('id', { type: () => Int }) id: number) {
    return this.placesService.findOne(id);
  }

  @Mutation(() => PlaceEntity)
  removePlace(@Args('id', { type: () => Int }) id: number) {
    return this.placesService.remove(id);
  }

  @ResolveField(() => LocationEntity)
  async location(@Parent() place: PlaceEntity): Promise<LocationEntity> {
    return await this.locationService.findOne(place.locationId);
  }

  @ResolveField(() => CategoryEntity)
  async category(@Parent() place: PlaceEntity): Promise<CategoryEntity> {
    return await this.categoriesService.findOne(place.categoryId);
  }

  @ResolveField(() => [ImageEntity])
  async images(@Parent() place: PlaceEntity): Promise<ImageEntity[]> {
    return await this.imagesService.findByPlace(place.id);
  }
}
