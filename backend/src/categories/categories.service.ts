import { Injectable } from '@nestjs/common';
import { CreateCategoryInput } from './dto/create-category.input';
import { Repository } from 'typeorm';
import { CategoryEntity } from '../typeorm/entities/category.entity';
import { InjectRepository } from '@nestjs/typeorm';

@Injectable()
export class CategoriesService {
  constructor(
    @InjectRepository(CategoryEntity)
    private readonly categoriesRepository: Repository<CategoryEntity>,
  ) {}
  async create(
    createCategoryInput: CreateCategoryInput,
  ): Promise<CategoryEntity> {
    const newCategory = this.categoriesRepository.create(createCategoryInput);
    return this.categoriesRepository.save(newCategory);
  }

  findAll(): Promise<CategoryEntity[]> {
    return this.categoriesRepository.find();
  }

  findOne(id: number): Promise<CategoryEntity> {
    return this.categoriesRepository.findOneByOrFail({ id: id });
  }

  async remove(id: number) {
    const old = await this.categoriesRepository.findOneOrFail({
      where: { id: id },
    });
    await this.categoriesRepository.delete({ id: id });
    return old;
  }
}
