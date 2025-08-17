package com.hainguyen.ecom.product.infrastructure.secondary.entity;

import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.aggregate.CategoryBuilder;
import com.hainguyen.ecom.product.domain.vo.CategoryName;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product_category", schema = "ecommerce")
@Builder
public class CategoryEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorySequence")
  @SequenceGenerator(name = "categorySequence", sequenceName = "product_category_sequence",
    allocationSize = 1, schema = "ecommerce")
  private Long id;

  @Column(unique = true, nullable = false)
  private UUID publicId;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "category")
  private Set<ProductEntity> products;

  public CategoryEntity() {
  }

  public CategoryEntity(Long id, UUID publicId, String name, Set<ProductEntity> products) {
    this.id = id;
    this.publicId = publicId;
    this.name = name;
    this.products = products;
  }

  // utility method
  public static Category toDomain(CategoryEntity categoryEntity) {
    CategoryBuilder builder = CategoryBuilder.category();
    return builder
      .dbId(categoryEntity.getId())
      .name(new CategoryName(categoryEntity.getName()))
      .publicId(new PublicId(categoryEntity.getPublicId()))
      .build();
  }

  public static CategoryEntity from(Category category) {
    CategoryEntityBuilder builder = CategoryEntityBuilder.categoryEntity();
    if (category.getDbId() != null) {
      builder.id(category.getDbId());
    }
    return builder.name(category.getName().value())
      .publicId(category.getPublicId().value())
      .build();
  }


  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<ProductEntity> getProducts() {
    return products;
  }

  public void setProducts(Set<ProductEntity> products) {
    this.products = products;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CategoryEntity that)) return false;
    return Objects.equals(publicId, that.publicId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(publicId);
  }
}
