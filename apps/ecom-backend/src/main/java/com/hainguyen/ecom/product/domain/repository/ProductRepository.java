package com.hainguyen.ecom.product.domain.repository;

import com.hainguyen.ecom.order.domain.order.vo.ProductPublicId;
import com.hainguyen.ecom.product.domain.aggregate.FilterQuery;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.vo.ProductSize;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

  Product save (Product productToCreate);

  Page<Product> findAll (Pageable pageable);

  int delete(PublicId publicId);

  Page<Product> findAllByFeaturedTrue(Pageable pageable);

  Optional<Product> findOne(PublicId publicId);

  Page<Product> findByCategoryExcludingOne(Pageable pageable,
                                           PublicId categoryPublicId,
                                           PublicId productPublicId);

  Page<Product> findByCategoryPublicIdAndSizeIn(Pageable pageable,
                                                FilterQuery filterQuery);

  List<Product> findByPublicIds(List<PublicId> publicIds);

  void updateNbInStockProduct(ProductPublicId productPublicId, long quantity);
}
