import { Resolver, Query, Mutation, Args, Int } from '@nestjs/graphql';
import { LocationService } from './location.service';
import { CreateLocationInput } from './dto/create-location.input';
import { LocationEntity } from '../typeorm/entities/location.entity';

@Resolver(() => LocationEntity)
export class LocationResolver {
  constructor(private readonly locationService: LocationService) {}

  @Mutation(() => LocationEntity)
  createLocation(
    @Args('createLocationInput') createLocationInput: CreateLocationInput,
  ) {
    return this.locationService.create(createLocationInput);
  }

  @Query(() => [LocationEntity], { name: 'locations' })
  findAll() {
    return this.locationService.findAll();
  }

  @Query(() => LocationEntity, { name: 'location' })
  findOne(@Args('id', { type: () => Int }) id: number) {
    return this.locationService.findOne(id);
  }

  @Mutation(() => LocationEntity)
  removeLocation(@Args('id', { type: () => Int }) id: number) {
    return this.locationService.remove(id);
  }
}
