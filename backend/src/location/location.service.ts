import { Injectable } from '@nestjs/common';
import { CreateLocationInput } from './dto/create-location.input';
import { Repository } from 'typeorm';
import { LocationEntity } from '../typeorm/entities/location.entity';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class LocationService {
  constructor(
    @InjectRepository(LocationEntity)
    private readonly locationRepository: Repository<LocationEntity>,
  ) {}
  async create(
    createLocationInput: CreateLocationInput,
  ): Promise<LocationEntity> {
    const newLocation = this.locationRepository.create(createLocationInput);
    return this.locationRepository.save(newLocation);
  }

  findAll() {
    return this.locationRepository.find();
  }

  findOne(id: number) {
    return this.locationRepository.findOneOrFail({ where: { id: id } });
  }

  remove(id: number) {
    return this.locationRepository.delete({ id: id });
  }
}
