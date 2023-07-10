import { Injectable } from '@nestjs/common';
import { CreateImageInput } from './dto/create-image.input';
import { Repository } from 'typeorm';
import { ImageEntity } from '../typeorm/entities/image.entity';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class ImagesService {
  constructor(
    @InjectRepository(ImageEntity)
    private readonly imagesRepository: Repository<ImageEntity>,
  ) {}
  create(createImageInput: CreateImageInput) {
    const image = this.imagesRepository.create(createImageInput);
    return this.imagesRepository.save(image);
  }

  findByPlace(placeId: number) {
    return this.imagesRepository
      .createQueryBuilder('image')
      .leftJoinAndSelect('image.place', 'place')
      .where('place.id = :placeId', { placeId: placeId })
      .getMany();
  }

  findOne(id: number) {
    return this.imagesRepository.findOneOrFail({ where: { id: id } });
  }

  async remove(id: number) {
    const old = await this.imagesRepository.findOneOrFail({
      where: { id: id },
    });
    await this.imagesRepository.delete({ id: id });
    return old;
  }

  async savaAll(input: CreateImageInput[]): Promise<ImageEntity[]> {
    const images = input.map((img) => this.imagesRepository.create(img));
    return await this.imagesRepository.save(images);
  }
}
