package com.hainguyen.ecom.order.infrastructure.secondary.repository;

import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import com.hainguyen.ecom.order.infrastructure.secondary.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {

  @Modifying
  @Query("UPDATE OrderEntity order SET order.status= :status, order.lastModifiedDate = CURRENT_TIMESTAMP " +
    "WHERE order.publicId = :publicId")
  void updateStatusByPublicId(OrderStatus status, UUID publicId);

  Optional<OrderEntity> findByStripeSessionId(String stripeSessionId);

  Page<OrderEntity> findAllByUserPublicId(Pageable pageable, UUID userPublicId);
}
