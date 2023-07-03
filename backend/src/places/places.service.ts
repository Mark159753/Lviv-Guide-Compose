import { Injectable } from '@nestjs/common';
import { PlaceEntity } from '../typeorm/entities/place.entity';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { CreatePlaceInput } from './dto/create-place.input';
import { LocationService } from '../location/location.service';

@Injectable()
export class PlacesService {
  constructor(
    @InjectRepository(PlaceEntity)
    private readonly placesRepository: Repository<PlaceEntity>,
    private readonly locationService: LocationService,
  ) {}

  async create(createPlaceInput: CreatePlaceInput): Promise<PlaceEntity> {
    const newLocation = await this.locationService.create(
      createPlaceInput.location,
    );
    delete createPlaceInput.location;
    const place = {
      ...createPlaceInput,
      location: newLocation,
    };
    const newPlace = this.placesRepository.create(place);
    return this.placesRepository.save(newPlace);
  }
  async findAll(): Promise<PlaceEntity[]> {
    return this.placesRepository.find();
  }

  findOne(id: number): Promise<PlaceEntity> {
    return this.placesRepository.findOneOrFail({ where: { id: id } });
  }

  remove(id: number) {
    return this.placesRepository.delete({ id: id });
  }
}
