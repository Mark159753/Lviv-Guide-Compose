import { Injectable } from '@nestjs/common';
import { PlaceEntity } from '../typeorm/entities/place.entity';
import { Repository } from 'typeorm';
import { InjectRepository } from '@nestjs/typeorm';
import { CreatePlaceInput } from './dto/create-place.input';
import { LocationService } from '../location/location.service';
import { FileUpload } from 'graphql-upload';
import { createWriteStream } from 'fs';
import { CategoriesService } from '../categories/categories.service';
import { ImagesService } from '../images/images.service';
import { CreateImageInput } from '../images/dto/create-image.input';
import * as crypto from 'crypto';
import * as pathLib from 'path';
import { FetchPlacesArgs } from './dto/fetch-places.args';

@Injectable()
export class PlacesService {
  constructor(
    @InjectRepository(PlaceEntity)
    private readonly placesRepository: Repository<PlaceEntity>,
    private readonly locationService: LocationService,
    private readonly categoryService: CategoriesService,
    private readonly imagesService: ImagesService,
  ) {}

  async create(createPlaceInput: CreatePlaceInput): Promise<PlaceEntity> {
    const headImage = await this.uploadFile(createPlaceInput.headImage);
    const images = await this.uploadAllFiles(createPlaceInput.images);
    const imagesInput = images.map((path) => {
      const input = new CreateImageInput();
      input.path = path;
      return input;
    });
    const imagesEntity = await this.imagesService.savaAll(imagesInput);

    const newLocation = await this.locationService.create(
      createPlaceInput.location,
    );
    const category = await this.categoryService.findOne(
      createPlaceInput.categoryId,
    );

    delete createPlaceInput.location;
    delete createPlaceInput.headImage;
    delete createPlaceInput.categoryId;

    const place = {
      ...createPlaceInput,
      location: newLocation,
      headImage: headImage,
      category: category,
      images: imagesEntity,
    };
    const newPlace = this.placesRepository.create(place);
    return this.placesRepository.save(newPlace);
  }
  async findAll(args: FetchPlacesArgs): Promise<PlaceEntity[]> {
    const skip = (args.page - 1) * args.size;
    return this.placesRepository.find({
      where: {
        categoryId: args.categoryId,
      },
      skip: skip,
      take: args.size,
    });
  }

  findOne(id: number): Promise<PlaceEntity> {
    return this.placesRepository.findOneOrFail({ where: { id: id } });
  }

  async remove(id: number) {
    const old = await this.placesRepository.findOneOrFail({
      where: { id: id },
    });
    await this.placesRepository.delete({ id: id });
    return old;
  }

  private async uploadFile(file: Promise<FileUpload>): Promise<string> {
    const { createReadStream, filename } = await file;

    const uniqueFilename = `${crypto.randomUUID()}${pathLib.extname(filename)}`;
    const path = `uploads/${uniqueFilename}`;

    return new Promise((resolve, reject) =>
      createReadStream()
        .pipe(createWriteStream(path))
        .on('finish', () => resolve(path))
        .on('error', (error) => {
          console.error(error);
          reject(error);
        }),
    );
  }

  private async uploadAllFiles(
    files: Promise<FileUpload>[],
  ): Promise<string[]> {
    return Promise.all(
      files.map(async (img: Promise<FileUpload>) => this.uploadFile(img)),
    );
  }
}
