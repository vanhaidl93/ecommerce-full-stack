package com.hainguyen.ecom.order.domain.order.service;

import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.aggregate.OrderedProduct;
import com.hainguyen.ecom.order.domain.order.repository.OrderRepository;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.infrastructure.secondary.service.stripe.StripeService;
import com.hainguyen.ecom.product.domain.aggregate.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderCreator {

  private final OrderRepository orderRepository;
  private final StripeService stripeService;

  public OrderCreator(OrderRepository orderRepository, StripeService stripeService) {
    this.orderRepository = orderRepository;
    this.stripeService = stripeService;
  }

  public StripeSessionId create(List<Product> products,
                                List<DetailCartItemRequest> cartItemRequests,
                                User connectedUser){
    StripeSessionId stripeSessionId =
                      this.stripeService.createPayment(products, cartItemRequests, connectedUser);
    List<OrderedProduct>  orderedProducts = new ArrayList<>();

    // mapping cartItemsRequest to Products
    cartItemRequests.forEach(item -> {
      Product mappedProduct = products.stream().filter(product ->
          product.getPublicId().value().equals(item.productPublicId().value())
        ).findFirst().orElseThrow();

      orderedProducts.add(OrderedProduct.create(item.quantity(),mappedProduct));
    });

    Order orderToCreate =Order.create(connectedUser,orderedProducts,stripeSessionId);
    orderRepository.save(orderToCreate);
    return stripeSessionId;
  }

}
