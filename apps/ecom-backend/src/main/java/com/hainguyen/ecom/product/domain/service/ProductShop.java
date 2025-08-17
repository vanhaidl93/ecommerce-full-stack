package com.hainguyen.ecom.product.domain.service;

import com.hainguyen.ecom.product.domain.aggregate.FilterQuery;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.repository.ProductRepository;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ProductShop {

  private final ProductRepository productRepository;

  public ProductShop(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<Product> findAllByFeaturedTrue(Pageable pageable) {
    return productRepository.findAllByFeaturedTrue(pageable);
  }

  public Page<Product> findRelated(Pageable pageable, PublicId productPublicId) {
    Optional<Product> productOpt = productRepository.findOne(productPublicId);
    if(productOpt.isPresent()) {
      Product product = productOpt.get();
      return productRepository.findByCategoryExcludingOne(pageable,product.getCategory().getPublicId(),productPublicId);
    }else {
      throw new EntityNotFoundException(String.format("No product found with id %s",productPublicId));
    }
  }

  public Page<Product> filter (Pageable pageable, FilterQuery query) {
    return productRepository.findByCategoryPublicIdAndSizeIn(pageable, query);
  }

}
