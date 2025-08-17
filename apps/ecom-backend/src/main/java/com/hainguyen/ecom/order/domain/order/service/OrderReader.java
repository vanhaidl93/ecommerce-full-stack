package com.hainguyen.ecom.order.domain.order.service;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class OrderReader {

  private final OrderRepository orderRepository;

  public OrderReader(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public Page<Order> findAllByUserPublicId(Pageable pageable, UUID userPublicId) {
    return orderRepository.findAllByUserPublicId(pageable,userPublicId);
  }

  public Page<Order> findAll(Pageable pageable) {
    return orderRepository.findAll(pageable);
  }
}
