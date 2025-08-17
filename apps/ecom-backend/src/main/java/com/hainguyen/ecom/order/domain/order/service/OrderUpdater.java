package com.hainguyen.ecom.order.domain.order.service;

import com.hainguyen.ecom.order.domain.order.aggregate.*;
import com.hainguyen.ecom.order.domain.order.repository.OrderRepository;

import java.util.List;


public class OrderUpdater {

  private final OrderRepository orderRepository;

  public OrderUpdater(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public List<OrderedProduct> updateOrderFromStripe(StripeSessionInformation stripeSessionInformation) {
    Order existingOrder = orderRepository.findByStripeSessionId(stripeSessionInformation.stripeSessionId())
      .orElseThrow();
    // change status to 'PAID'
    existingOrder.validatePayment();

    orderRepository.updateStatusByPublicId(existingOrder.getStatus(), existingOrder.getPublicId());
    return existingOrder.getOrderedProducts();
  }

  public List<OrderProductQuantity> computedQuantity(List<OrderedProduct> orderedProducts) {
    return orderedProducts.stream().map(orderedProduct ->
        OrderProductQuantityBuilder.orderProductQuantity()
          .orderQuantity(orderedProduct.getQuantity())
          .productPublicId(orderedProduct.getProductPublicId())
          .build())
      .toList();
  }
}
