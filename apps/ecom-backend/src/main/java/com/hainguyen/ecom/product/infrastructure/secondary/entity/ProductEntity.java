package com.hainguyen.ecom.product.infrastructure.secondary.entity;

import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.aggregate.ProductBuilder;
import com.hainguyen.ecom.product.domain.vo.*;
import com.hainguyen.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product",schema = "ecommerce")
@Builder
public class ProductEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSequence")
  @SequenceGenerator(name = "productSequence", sequenceName = "product_sequence",
    allocationSize = 1, schema = "ecommerce")
  private Long id;

  private String brand;
  private String color;
  private String description;
  private String name;
  private double price;
  private boolean featured;

  @Enumerated(EnumType.STRING)
  private ProductSize size;

  @Column(unique = true)
  private UUID publicId;
  private int nbInStock;

  @ManyToOne
  @JoinColumn(name = "category_fk",referencedColumnName = "id")
  private CategoryEntity category;

  @OneToMany(mappedBy = "product")
  private Set<PictureEntity> pictures = new HashSet<>();


  public ProductEntity() {
  }

  public ProductEntity(Long id, String brand, String color, String description, String name, double price, boolean featured, ProductSize size, UUID publicId, int nbInStock, CategoryEntity category, Set<PictureEntity> pictures) {
    this.id = id;
    this.brand = brand;
    this.color = color;
    this.description = description;
    this.name = name;
    this.price = price;
    this.featured = featured;
    this.size = size;
    this.publicId = publicId;
    this.nbInStock = nbInStock;
    this.category = category;
    this.pictures = pictures;
  }

  // utility
  public static ProductEntity from(Product product) {
    var builder = ProductEntityBuilder.productEntity();
    if(product.getDbId() != null){
      builder.id(product.getDbId());
    }
    return builder
      .brand(product.getBrand().value())
      .color(product.getColor().value())
      .description(product.getDescription().value())
      .name(product.getName().value())
      .price(product.getPrice().value())
      .featured(product.getFeatured())
      .size(product.getSize())
      .publicId(product.getPublicId().value())
      .nbInStock(product.getNbInStock())
      .category(CategoryEntity.from(product.getCategory()))
      .pictures(PictureEntity.from(product.getPictures()))
      .build();
  }

  public static Product to(ProductEntity productEntity) {
    return ProductBuilder.product()
      .dbId(productEntity.getId())
      .brand(new ProductBrand(productEntity.getBrand()))
      .color(new ProductColor(productEntity.getColor()))
      .description(new ProductDescription(productEntity.getDescription()))
      .name(new ProductName(productEntity.getName()))
      .price(new ProductPrice(productEntity.getPrice()))
      .featured(productEntity.isFeatured())
      .size(productEntity.getSize())
      .publicId(new PublicId(productEntity.getPublicId()))
      .nbInStock(productEntity.getNbInStock())
      .category(CategoryEntity.toDomain(productEntity.getCategory()))
      .pictures(PictureEntity.toDomain(productEntity.getPictures()))
      .build();
  }



  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean isFeatured() {
    return featured;
  }

  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  public ProductSize getSize() {
    return size;
  }

  public void setSize(ProductSize size) {
    this.size = size;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public int getNbInStock() {
    return nbInStock;
  }

  public void setNbInStock(int nbInStock) {
    this.nbInStock = nbInStock;
  }

  public CategoryEntity getCategory() {
    return category;
  }

  public void setCategory(CategoryEntity category) {
    this.category = category;
  }

  public Set<PictureEntity> getPictures() {
    return pictures;
  }

  public void setPictures(Set<PictureEntity> pictures) {
    this.pictures = pictures;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ProductEntity that)) return false;
    return Double.compare(price, that.price) == 0 && featured == that.featured && Objects.equals(brand, that.brand) && Objects.equals(color, that.color) && Objects.equals(description, that.description) && Objects.equals(name, that.name) && size == that.size && Objects.equals(publicId, that.publicId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, color, description, name, price, featured, size, publicId);
  }
}
