package com.hainguyen.ecom.product.infrastructure.secondary.repository;

import com.hainguyen.ecom.product.domain.vo.ProductSize;
import com.hainguyen.ecom.product.infrastructure.secondary.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

  int deleteByPublicId(UUID publicId);

  Optional<ProductEntity> findByPublicId(UUID publicId);

  Page<ProductEntity> findAllByFeaturedTrue(Pageable pageable);

  // "other products in the same category" — but exclude the current one. related products
  Page<ProductEntity> findByCategoryPublicIdAndPublicIdNot(Pageable pageable,
                                                           UUID categoryPublicId,
                                                           UUID publicId);

  @Query("SELECT product FROM ProductEntity product " +
    "WHERE (:sizes is null or product.size IN (:sizes)) " +
    "AND product.category.publicId = :categoryPublicId")
  Page<ProductEntity> findByCategoryPublicIdAndSizeIn(Pageable pageable,
                                                      UUID categoryPublicId,
                                                      List<ProductSize> sizes);

  List<ProductEntity> findAllByPublicIdIn(List<UUID> publicIds);

  @Modifying
  @Query("UPDATE ProductEntity  product " +
    "SET product.nbInStock = product.nbInStock - :quantity, product.lastModifiedDate = CURRENT_TIMESTAMP " +
    "WHERE product.publicId= :productPublicId")
  void updateNbInStockProduct(UUID productPublicId, long quantity);



}
