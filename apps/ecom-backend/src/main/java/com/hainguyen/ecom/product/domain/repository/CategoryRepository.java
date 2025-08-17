package com.hainguyen.ecom.product.domain.repository;

import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {

  Page<Category> findAll(Pageable pageable);

  int delete(PublicId publicId);

  Category save(Category categoryToCreate);
}
