import { Module } from '@nestjs/common';
import { PlacesService } from './places.service';
import { PlacesResolver } from './places.resolver';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PlaceEntity } from '../typeorm/entities/place.entity';
import { LocationModule } from '../location/location.module';
import { CategoriesModule } from '../categories/categories.module';

@Module({
  imports: [
    TypeOrmModule.forFeature([PlaceEntity]),
    LocationModule,
    CategoriesModule,
  ],
  providers: [PlacesService, PlacesResolver],
  exports: [PlacesService],
})
export class PlacesModule {}
