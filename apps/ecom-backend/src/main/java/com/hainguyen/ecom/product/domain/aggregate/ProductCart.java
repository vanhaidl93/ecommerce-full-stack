package com.hainguyen.ecom.product.domain.aggregate;

import com.hainguyen.ecom.product.domain.vo.ProductBrand;
import com.hainguyen.ecom.product.domain.vo.ProductName;
import com.hainguyen.ecom.product.domain.vo.ProductPrice;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

@Builder
public class ProductCart {
  private PublicId publicId;
  private ProductName name;
  private ProductPrice price;
  private ProductBrand brand;
  private Picture picture;

  public ProductCart() {
  }

  public ProductCart(PublicId publicId, ProductName name, ProductPrice price, ProductBrand brand, Picture picture) {
    assertFields(publicId, name, price, brand, picture);
    this.publicId = publicId;
    this.name = name;
    this.price = price;
    this.brand = brand;
    this.picture = picture;
  }

  private void assertFields(PublicId publicId, ProductName name, ProductPrice price, ProductBrand brand, Picture picture) {
    Assert.notNull("publicId", publicId);
    Assert.notNull("name", name);
    Assert.notNull("price", price);
    Assert.notNull("brand", brand);
    Assert.notNull("picture", picture);
  }

  // Utility methods
  public static ProductCart from(Product product) {
    return ProductCartBuilder.productCart()
      .publicId(product.getPublicId())
      .name(product.getName())
      .price(product.getPrice())
      .brand(product.getBrand())
      .picture(product.getPictures().stream().findFirst().orElseThrow())
      .build();
  }


  public PublicId getPublicId() {
    return publicId;
  }

  public ProductName getName() {
    return name;
  }

  public ProductPrice getPrice() {
    return price;
  }

  public ProductBrand getBrand() {
    return brand;
  }

  public Picture getPicture() {
    return picture;
  }
}
