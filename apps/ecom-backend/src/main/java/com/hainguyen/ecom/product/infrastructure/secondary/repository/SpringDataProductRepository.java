package com.hainguyen.ecom.product.infrastructure.secondary.repository;

import com.hainguyen.ecom.order.domain.order.vo.ProductPublicId;
import com.hainguyen.ecom.product.domain.aggregate.FilterQuery;
import com.hainguyen.ecom.product.domain.aggregate.Picture;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.repository.ProductRepository;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.product.infrastructure.secondary.entity.CategoryEntity;
import com.hainguyen.ecom.product.infrastructure.secondary.entity.PictureEntity;
import com.hainguyen.ecom.product.infrastructure.secondary.entity.ProductEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SpringDataProductRepository implements ProductRepository {

  private final JpaProductRepository jpaProductRepository;
  private final JpaCategoryRepository jpaCategoryRepository;
  private final JpaProductPictureRepository jpaProductPictureRepository;

  public SpringDataProductRepository(JpaProductRepository jpaProductRepository, JpaCategoryRepository jpaCategoryRepository, JpaProductPictureRepository jpaProductPicture) {
    this.jpaProductRepository = jpaProductRepository;
    this.jpaCategoryRepository = jpaCategoryRepository;
    this.jpaProductPictureRepository = jpaProductPicture;
  }

  @Override
  public Product save(Product productToCreate) {
    ProductEntity newProductEntity = ProductEntity.from(productToCreate);

    // set category into product ( save product - save on child)
    CategoryEntity categoryEntity = jpaCategoryRepository.findByPublicId(newProductEntity.getCategory().getPublicId())
      .orElseThrow(() -> new EntityNotFoundException(String.format("No category found with id %s", productToCreate.getCategory().getPublicId())));
    newProductEntity.setCategory(categoryEntity);
    var savedProductEntity = jpaProductRepository.save(newProductEntity);
    // save product into each picture ( saveAll pictures - save on child)
    saveAllPictures(productToCreate.getPictures(), newProductEntity);

    return ProductEntity.to(savedProductEntity);
  }

  private void saveAllPictures(Set<Picture> pictures, ProductEntity newProductEntity) {
    Set<PictureEntity> newPictureEntities = PictureEntity.from(pictures);
    newPictureEntities.forEach(pictureEntity -> pictureEntity.setProduct(newProductEntity));
    jpaProductPictureRepository.saveAll(newPictureEntities);
  }

  @Override
  public Page<Product> findAll(Pageable pageable) {
    return jpaProductRepository.findAll(pageable).map(ProductEntity::to);
  }

  @Override
  public int delete(PublicId publicId) {
    return jpaProductRepository.deleteByPublicId(publicId.value());
  }

  @Override
  public Page<Product> findAllByFeaturedTrue(Pageable pageable) {
    return jpaProductRepository.findAllByFeaturedTrue(pageable).map(ProductEntity::to);
  }

  @Override
  public Optional<Product> findOne(PublicId publicId) {
    return jpaProductRepository.findByPublicId(publicId.value()).map(ProductEntity::to);
  }

  @Override
  public Page<Product> findByCategoryExcludingOne(Pageable pageable, PublicId categoryPublicId, PublicId productPublicId) {
    return jpaProductRepository.findByCategoryPublicIdAndPublicIdNot(
      pageable,
      categoryPublicId.value(),
      productPublicId.value()
    ).map(ProductEntity::to);
  }

  @Override
  public Page<Product> findByCategoryPublicIdAndSizeIn(Pageable pageable, FilterQuery filterQuery) {
    return jpaProductRepository.findByCategoryPublicIdAndSizeIn(
      pageable, filterQuery.categoryPublicId().value(), filterQuery.sizes())
      .map(ProductEntity::to);
  }

  @Override
  public List<Product> findByPublicIds(List<PublicId> publicIds) {
    List<UUID> uuids = publicIds.stream().map(PublicId::value).toList();
    return jpaProductRepository.findAllByPublicIdIn(uuids)
      .stream().map(ProductEntity::to).collect(Collectors.toList());
  }

  @Override
  public void updateNbInStockProduct(ProductPublicId productPublicId, long quantity) {
    jpaProductRepository.updateNbInStockProduct(productPublicId.value(), quantity);
  }
}
