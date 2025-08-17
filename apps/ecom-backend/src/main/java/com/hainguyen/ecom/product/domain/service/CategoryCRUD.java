package com.hainguyen.ecom.product.domain.service;

import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.repository.CategoryRepository;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CategoryCRUD {

  private final CategoryRepository categoryRepository;

  public CategoryCRUD(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Category save(Category categoryToCreate) {
    categoryToCreate.initDefaultFields();
    return categoryRepository.save(categoryToCreate);
  }

  public Page<Category> findAll(Pageable pageable) {
    return categoryRepository.findAll(pageable);
  }

  public PublicId delete(PublicId categoryId) {
    int nbOfRowsDeleted = categoryRepository.delete(categoryId);
    if(nbOfRowsDeleted !=1){
      throw new EntityNotFoundException(String.format("No category deleted with id: %s", categoryId));
    }
    return categoryId;
  }
}
