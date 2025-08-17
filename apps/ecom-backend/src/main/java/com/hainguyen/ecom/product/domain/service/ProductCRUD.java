package com.hainguyen.ecom.product.domain.service;

import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.repository.ProductRepository;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ProductCRUD {

  private final ProductRepository productRepository;

  public ProductCRUD(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product save(Product productToCreate) {
    productToCreate.initDefaultFields();
    return productRepository.save(productToCreate);
  }

  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  public PublicId delete(PublicId publicId) {
    int nbOfRowsDeleted = productRepository.delete(publicId);
    if (nbOfRowsDeleted != 1) {
      throw new EntityNotFoundException(String.format("No product deleted with id: %s", publicId));
    }
    return publicId;
  }

  public Optional<Product> findOne(PublicId publicId) {
    return productRepository.findOne(publicId);
  }

  public List<Product> findAllByPublicIdIn(List<PublicId> publicIds){
    return productRepository.findByPublicIds(publicIds);
  }


}
