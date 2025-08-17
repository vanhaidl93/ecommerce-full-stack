package com.hainguyen.ecom.product.infrastructure.secondary.repository;

import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.repository.CategoryRepository;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.product.infrastructure.secondary.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDataCategoryRepository implements CategoryRepository {

  private final JpaCategoryRepository jpaCategoryRepository;

  public SpringDataCategoryRepository(JpaCategoryRepository jpaCategoryRepository) {
    this.jpaCategoryRepository = jpaCategoryRepository;
  }

  @Override
  public Page<Category> findAll(Pageable pageable) {
    return jpaCategoryRepository.findAll(pageable).map(CategoryEntity::toDomain);
  }

  @Override
  public int delete(PublicId publicId) {
    return jpaCategoryRepository.deleteByPublicId(publicId.value());
  }

  @Override
  public Category save(Category categoryToCreate) {
    var categoryEntitySaved = jpaCategoryRepository.save(CategoryEntity.from(categoryToCreate));
    return CategoryEntity.toDomain(categoryEntitySaved);
  }
}
