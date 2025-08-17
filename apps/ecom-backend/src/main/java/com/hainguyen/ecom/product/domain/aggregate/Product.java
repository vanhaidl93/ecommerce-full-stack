package com.hainguyen.ecom.product.domain.aggregate;

import com.hainguyen.ecom.product.domain.vo.*;
import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public class Product {

  private final ProductBrand brand;
  private final ProductColor color;
  private final ProductDescription description;
  private final ProductName name;
  private final ProductPrice price;
  private final ProductSize size;
  private final Category category;
  private final Set<Picture> pictures;

  private Long dbId;
  private Boolean featured;
  private PublicId publicId;
  private int nbInStock; // number in stock.

  public Product(ProductBrand brand, ProductColor color, ProductDescription description, ProductName name, ProductPrice price, ProductSize size, Category category, Set<Picture> pictures, Long dbId, boolean featured, PublicId publicId, int nbInStock) {
    assertMandatoryFields(brand,color,description,name,price,size,category,pictures,featured,nbInStock);
    this.brand = brand;
    this.color = color;
    this.description = description;
    this.name = name;
    this.price = price;
    this.category = category;
    this.pictures = pictures;
    this.dbId = dbId;
    this.featured = featured;
    this.publicId = publicId;
    this.nbInStock = nbInStock;
    this.size = size;
  }

  public void initDefaultFields(){
    this.publicId = new PublicId(UUID.randomUUID());
  }

  private void assertMandatoryFields(ProductBrand brand, ProductColor color, ProductDescription description, ProductName name, ProductPrice price, ProductSize size, Category category, Set<Picture> pictures, boolean featured, int nbInStock) {
    Assert.notNull("brand", brand);
    Assert.notNull("color", color);
    Assert.notNull("description", description);
    Assert.notNull("name", name);
    Assert.notNull("price", price);
    Assert.notNull("size", size);
    Assert.notNull("category", category);
    Assert.notNull("pictures", pictures);
    Assert.notNull("featured", featured);
    assertNumberInStock(nbInStock);
  }

  private void assertNumberInStock(int input){
    if (input < 0) {
      throw new IllegalArgumentException("nbInStock cannot be negative");
    }
  }

  public ProductBrand getBrand() {
    return brand;
  }

  public ProductColor getColor() {
    return color;
  }

  public ProductDescription getDescription() {
    return description;
  }

  public ProductName getName() {
    return name;
  }

  public ProductPrice getPrice() {
    return price;
  }

  public Category getCategory() {
    return category;
  }

  public Set<Picture> getPictures() {
    return pictures;
  }

  public Long getDbId() {
    return dbId;
  }

  public Boolean isFeatured() {
    return featured;
  }

  public PublicId getPublicId() {
    return publicId;
  }

  public int getNbInStock() {
    return nbInStock;
  }

  public ProductSize getSize() {
    return size;
  }

  public Boolean getFeatured() {
    return featured;
  }
}
