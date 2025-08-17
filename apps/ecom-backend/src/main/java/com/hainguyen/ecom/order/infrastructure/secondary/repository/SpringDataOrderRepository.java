package com.hainguyen.ecom.order.infrastructure.secondary.repository;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.repository.OrderRepository;
import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.infrastructure.secondary.entity.OrderEntity;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SpringDataOrderRepository implements OrderRepository {

  private final JpaOrderRepository jpaOrderRepository;
  private final JpaOrderedProductRepository jpaOrderedProductRepository;

  public SpringDataOrderRepository(JpaOrderRepository jpaOrderRepository, JpaOrderedProductRepository jpaOrderedProductRepository) {
    this.jpaOrderRepository = jpaOrderRepository;
    this.jpaOrderedProductRepository = jpaOrderedProductRepository;
  }

  @Override
  public void save(Order order) {
    OrderEntity orderEntityToCreate = OrderEntity.fromDomain(order);
    // save parent
    OrderEntity savedOrderEntity = jpaOrderRepository.save(orderEntityToCreate);

    // save childes manually.
    savedOrderEntity.getOrderedProducts().forEach(
      orderedProductEntity -> orderedProductEntity.getId().setOrder(savedOrderEntity));
    jpaOrderedProductRepository.saveAll(savedOrderEntity.getOrderedProducts());
  }

  @Override
  public void updateStatusByPublicId(OrderStatus status, PublicId publicId) {
    jpaOrderRepository.updateStatusByPublicId(status,publicId.value());
  }

  @Override
  public Optional<Order> findByStripeSessionId(StripeSessionId stripeSessionId) {
    return jpaOrderRepository.findByStripeSessionId(stripeSessionId.value())
      .map(OrderEntity::toDomain);
  }

  @Override
  public Page<Order> findAllByUserPublicId(Pageable pageable, UUID userPublicId) {
    return jpaOrderRepository.findAllByUserPublicId(pageable,userPublicId).map(OrderEntity::toDomain);
  }

  @Override
  public Page<Order> findAll(Pageable pageable) {
    return jpaOrderRepository.findAll(pageable).map(OrderEntity::toDomain);
  }
}
