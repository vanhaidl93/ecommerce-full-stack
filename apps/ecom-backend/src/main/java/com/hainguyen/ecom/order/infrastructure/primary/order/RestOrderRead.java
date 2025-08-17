package com.hainguyen.ecom.order.infrastructure.primary.order;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestOrderRead(UUID publicId,
                            OrderStatus orderStatus,
                            List<RestOrderedItemRead> orderedItems) {

  public static RestOrderRead fromDomain(Order order) {
    return RestOrderReadBuilder.restOrderRead()
      .publicId(order.getPublicId().value())
      .orderStatus(order.getStatus())
      .orderedItems(RestOrderedItemRead.fromDomain(order.getOrderedProducts()))
      .build();
  }
}
