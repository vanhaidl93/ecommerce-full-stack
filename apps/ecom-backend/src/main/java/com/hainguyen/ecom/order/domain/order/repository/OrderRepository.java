package com.hainguyen.ecom.order.domain.order.repository;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
  void save(Order order);

  void updateStatusByPublicId(OrderStatus status, PublicId publicId);

  Optional<Order> findByStripeSessionId(StripeSessionId stripeSessionId);

  Page<Order> findAllByUserPublicId(Pageable pageable, UUID userPublicId);

  Page<Order> findAll(Pageable pageable);
}
