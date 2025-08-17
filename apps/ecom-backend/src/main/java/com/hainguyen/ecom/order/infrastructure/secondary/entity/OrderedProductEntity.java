package com.hainguyen.ecom.order.infrastructure.secondary.entity;

import com.hainguyen.ecom.order.domain.order.aggregate.OrderedProduct;
import com.hainguyen.ecom.order.domain.order.aggregate.OrderedProductBuilder;
import com.hainguyen.ecom.order.domain.order.vo.OrderPrice;
import com.hainguyen.ecom.order.domain.order.vo.OrderQuantity;
import com.hainguyen.ecom.order.domain.order.vo.ProductPublicId;
import com.hainguyen.ecom.product.domain.vo.ProductName;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.jilt.Builder;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ordered_product", schema = "ecommerce")
@Builder
public class OrderedProductEntity {

  @EmbeddedId
  private OrderedProductEntityPK id;

  @Column(nullable = false)
  private Double price;
  @Column(nullable = false)
  private long quantity;
  @Column(nullable = false)
  private String productName;

  public OrderedProductEntity() {
  }

  public OrderedProductEntity(OrderedProductEntityPK id, Double price, long quantity, String productName) {
    this.id = id;
    this.price = price;
    this.quantity = quantity;
    this.productName = productName;
  }

  // utility methods
  public static OrderedProductEntity fromDomain(OrderedProduct orderedProduct) {
    OrderedProductEntityPK compositedPk = OrderedProductEntityPKBuilder.orderedProductEntityPK()
      .productPublicId(orderedProduct.getProductPublicId().value())
      .build();
    return OrderedProductEntityBuilder.orderedProductEntity()
      .id(compositedPk)
      .price(orderedProduct.getPrice().value())
      .quantity(orderedProduct.getQuantity().value())
      .productName(orderedProduct.getProductName().value())
      .build();
  }

  public static List<OrderedProductEntity> fromDomain(List<OrderedProduct> orderedProducts) {
    return  orderedProducts.stream().map(OrderedProductEntity::fromDomain).toList();
  }

  public static OrderedProduct toDomain(OrderedProductEntity orderedProductEntity) {
    return OrderedProductBuilder.orderedProduct()
      .productPublicId(new ProductPublicId(orderedProductEntity.getId().getProductPublicId()))
      .price(new OrderPrice(orderedProductEntity.getPrice()))
      .productName(new ProductName(orderedProductEntity.getProductName()))
      .quantity(new OrderQuantity(orderedProductEntity.getQuantity()))
      .build();
  }

  public static List<OrderedProduct> toDomain(List<OrderedProductEntity> orderedProductEntities) {
    return orderedProductEntities.stream().map(OrderedProductEntity::toDomain).toList();
  }

  public OrderedProductEntityPK getId() {
    return id;
  }

  public void setId(OrderedProductEntityPK id) {
    this.id = id;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof OrderedProductEntity that)) return false;
    return quantity == that.quantity && Objects.equals(price, that.price) && Objects.equals(productName, that.productName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(price, quantity, productName);
  }
}
