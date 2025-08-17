package com.hainguyen.ecom.order.infrastructure.primary.order;

import com.hainguyen.ecom.order.domain.order.aggregate.OrderedProduct;
import org.jilt.Builder;

import java.util.List;

@Builder
public record RestOrderedItemRead(long quantity,
                                  double price,
                                  String name) {

  public static RestOrderedItemRead fromDomain(OrderedProduct orderedProduct) {

    return RestOrderedItemReadBuilder.restOrderedItemRead()
      .quantity(orderedProduct.getQuantity().value())
      .price(orderedProduct.getPrice().value())
      .name(orderedProduct.getProductName().value())
      .build();
  }

  public static List<RestOrderedItemRead> fromDomain(List<OrderedProduct> orderedProducts) {
    return orderedProducts.stream().map(RestOrderedItemRead::fromDomain).toList();
  }
}
