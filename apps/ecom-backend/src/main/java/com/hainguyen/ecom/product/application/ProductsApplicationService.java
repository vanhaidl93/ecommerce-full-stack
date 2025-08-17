package com.hainguyen.ecom.product.application;

import com.hainguyen.ecom.order.domain.order.aggregate.OrderProductQuantity;
import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.aggregate.FilterQuery;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.repository.CategoryRepository;
import com.hainguyen.ecom.product.domain.repository.ProductRepository;
import com.hainguyen.ecom.product.domain.service.CategoryCRUD;
import com.hainguyen.ecom.product.domain.service.ProductCRUD;
import com.hainguyen.ecom.product.domain.service.ProductShop;
import com.hainguyen.ecom.product.domain.service.ProductUpdate;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductsApplicationService {

  private final CategoryCRUD categoryCRUD;
  private final ProductCRUD productCRUD;
  private final ProductShop productShop;
  private final ProductUpdate productUpdate;

  public ProductsApplicationService(CategoryRepository categoryRepository, ProductRepository productRepository) {
    this.categoryCRUD = new CategoryCRUD(categoryRepository);
    this.productCRUD = new ProductCRUD(productRepository);
    this.productShop = new ProductShop(productRepository);
    this.productUpdate = new ProductUpdate(productRepository);
  }

  @Transactional
  public Product createProduct(Product newProduct){
    return productCRUD.save(newProduct);
  }

  @Transactional
  public Page<Product> findAllProducts(Pageable pageable){
    return productCRUD.findAll(pageable);
  }

  @Transactional
  public PublicId deleteProduct(PublicId productId){
    return productCRUD.delete(productId);
  }

  @Transactional
  public Category createCategory(Category newCategory){
    return categoryCRUD.save(newCategory);
  }

  @Transactional
  public PublicId deleteCategory(PublicId publicId){
    return categoryCRUD.delete(publicId);
  }

  @Transactional(readOnly = true)
  public Page<Category> findAllCategories(Pageable pageable){
    return categoryCRUD.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Page<Product> getFeaturedProducts(Pageable pageable){
    return productShop.findAllByFeaturedTrue(pageable);
  }

  @Transactional(readOnly = true)
  public Optional<Product> findOne(PublicId productPublicId){
    return  productCRUD.findOne(productPublicId);
  }

  @Transactional(readOnly = true)
  public Page<Product> findRelated(Pageable pageable, PublicId productPublicId){
    return productShop.findRelated(pageable,productPublicId);
  }

  @Transactional(readOnly = true)
  public Page<Product> filter(Pageable pageable, FilterQuery query){
    return productShop.filter(pageable,query);
  }

  @Transactional(readOnly = true)
  public List<Product> getProductByProductIds(List<PublicId> productIds){
    return productCRUD.findAllByPublicIdIn(productIds);
  }

  @Transactional
  public void updateNbInStockProduct(List<OrderProductQuantity> orderProductQuantities){
    productUpdate.updateNbInStockProduct(orderProductQuantities);
  }

}
