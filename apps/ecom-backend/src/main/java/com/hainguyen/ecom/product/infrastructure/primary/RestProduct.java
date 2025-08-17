package com.hainguyen.ecom.product.infrastructure.primary;

import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.aggregate.ProductBuilder;
import com.hainguyen.ecom.product.domain.vo.*;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public class RestProduct {

  private UUID publicId;
  private String brand;
  private String color;
  private String description;
  private String name;
  private double price;
  private ProductSize size;
  private boolean featured;
  private int nbInStock;

  private RestCategory category;
  private Set<RestPicture> pictures;

  public RestProduct() {
  }

  public RestProduct(UUID publicId, String brand, String color, String description, String name, double price, ProductSize size, boolean featured, int nbInStock, RestCategory category, Set<RestPicture> pictures) {
    this.publicId = publicId;
    this.brand = brand;
    this.color = color;
    this.description = description;
    this.name = name;
    this.price = price;
    this.size = size;
    this.featured = featured;
    this.nbInStock = nbInStock;
    this.category = category;
    this.pictures = pictures;
  }

  // utility
  public void addAllPictures(Set<RestPicture> pictures){
    this.pictures.addAll(pictures);
  }

  public static Product to(RestProduct restProduct){
    ProductBuilder builder = ProductBuilder.product();
    builder.brand(new ProductBrand(restProduct.getBrand()))
      .color(new ProductColor(restProduct.getColor()))
      .description(new ProductDescription(restProduct.getDescription()))
      .name(new ProductName(restProduct.getName()))
      .price(new ProductPrice(restProduct.getPrice()))
      .size(restProduct.getSize())
      .category(RestCategory.to(restProduct.getCategory()))
      .featured(restProduct.isFeatured())
      .nbInStock(restProduct.getNbInStock());

    if(restProduct.getPublicId() != null){
      builder.publicId(new PublicId(restProduct.getPublicId()));
    }
    if(restProduct.getPictures() != null){
      builder.pictures(RestPicture.to(restProduct.getPictures()));
    }
    return builder.build();
  }

  public static RestProduct from(Product product){
    return RestProductBuilder.restProduct()
      .brand(product.getBrand().value())
      .color(product.getColor().value())
      .description(product.getDescription().value())
      .name(product.getName().value())
      .price(product.getPrice().value())
      .size(product.getSize())
      .featured(product.isFeatured())
      .nbInStock(product.getNbInStock())
      .publicId(product.getPublicId().value())
      .category(RestCategory.from(product.getCategory()))
      .pictures(RestPicture.from(product.getPictures()))
      .build();
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
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

  public ProductSize getSize() {
    return size;
  }

  public void setSize(ProductSize size) {
    this.size = size;
  }

  public boolean isFeatured() {
    return featured;
  }

  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  public int getNbInStock() {
    return nbInStock;
  }

  public void setNbInStock(int nbInStock) {
    this.nbInStock = nbInStock;
  }

  public RestCategory getCategory() {
    return category;
  }

  public void setCategory(RestCategory category) {
    this.category = category;
  }

  public Set<RestPicture> getPictures() {
    return pictures;
  }

  public void setPictures(Set<RestPicture> pictures) {
    this.pictures = pictures;
  }
}
