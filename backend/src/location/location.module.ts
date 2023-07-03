import { Module } from '@nestjs/common';
import { LocationService } from './location.service';
import { LocationResolver } from './location.resolver';
import { TypeOrmModule } from '@nestjs/typeorm';
import { LocationEntity } from '../typeorm/entities/location.entity';

@Module({
  imports: [TypeOrmModule.forFeature([LocationEntity])],
  providers: [LocationResolver, LocationService],
  exports: [LocationService],
})
export class LocationModule {}
