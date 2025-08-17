package com.hainguyen.ecom.order.infrastructure.secondary.entity;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.aggregate.OrderBuilder;
import com.hainguyen.ecom.order.domain.order.aggregate.OrderedProduct;
import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "order", schema = "ecommerce")
@Builder
public class OrderEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderSequenceGenerator")
  @SequenceGenerator(name = "orderSequenceGenerator", sequenceName = "order_sequence",
    allocationSize = 1, schema = "ecommerce")
  private Long id;

  @Column(nullable = false)
  private UUID publicId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(nullable = false)
  private String stripeSessionId;

  @OneToMany(mappedBy = "id.order", cascade = CascadeType.REMOVE)
  private Set<OrderedProductEntity> orderedProducts = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "fk_customer", nullable = false)
  private UserEntity user;


  public OrderEntity() {
  }

  public OrderEntity(Long id, UUID publicId, OrderStatus status, String stripeSessionId, Set<OrderedProductEntity> orderedProducts, UserEntity user) {
    this.id = id;
    this.publicId = publicId;
    this.status = status;
    this.stripeSessionId = stripeSessionId;
    this.orderedProducts = orderedProducts;
    this.user = user;
  }

  // utility methods
  public static OrderEntity fromDomain(Order order) {
    Set<OrderedProductEntity> orderedProductEntities = order.getOrderedProducts().stream()
      .map(OrderedProductEntity::fromDomain).collect(Collectors.toSet());
    return OrderEntityBuilder.orderEntity()
      .publicId(order.getPublicId().value())
      .status(order.getStatus())
      .stripeSessionId(order.getStripeId())
      .orderedProducts(orderedProductEntities)
      .user(UserEntity.from(order.getUser()))
      .build();
  }

  public static Order toDomain(OrderEntity orderEntity) {
    List<OrderedProduct> orderedProducts = orderEntity.getOrderedProducts().stream()
      .map(OrderedProductEntity::toDomain).toList();
    return OrderBuilder.order()
      .publicId(new PublicId(orderEntity.getPublicId()))
      .status(orderEntity.getStatus())
      .stripeId(orderEntity.stripeSessionId)
      .orderedProducts(orderedProducts)
      .user(UserEntity.toDomain(orderEntity.user))
      .build();
  }

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

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public String getStripeSessionId() {
    return stripeSessionId;
  }

  public void setStripeSessionId(String stripeSessionId) {
    this.stripeSessionId = stripeSessionId;
  }

  public Set<OrderedProductEntity> getOrderedProducts() {
    return orderedProducts;
  }

  public void setOrderedProducts(Set<OrderedProductEntity> orderedProducts) {
    this.orderedProducts = orderedProducts;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof OrderEntity that)) return false;
    return Objects.equals(publicId, that.publicId) && status == that.status && Objects.equals(stripeSessionId, that.stripeSessionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicId, status, stripeSessionId);
  }
}
