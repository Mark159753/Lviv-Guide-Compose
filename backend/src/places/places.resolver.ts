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

@Resolver((of) => PlaceEntity)
export class PlacesResolver {
  constructor(
    private readonly placesService: PlacesService,
    private readonly locationService: LocationService,
    private readonly categoriesService: CategoriesService,
  ) {}

  @Mutation(() => PlaceEntity)
  createPlace(@Args('createPlaceInput') createPlaceInput: CreatePlaceInput) {
    return this.placesService.create(createPlaceInput);
  }

  @Query((returns) => [PlaceEntity])
  findAll(): Promise<PlaceEntity[]> {
    return this.placesService.findAll();
  }

  @Query(() => PlaceEntity, { name: 'category' })
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
}
