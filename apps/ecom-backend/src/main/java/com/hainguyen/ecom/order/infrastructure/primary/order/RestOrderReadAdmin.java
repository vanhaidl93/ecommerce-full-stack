package com.hainguyen.ecom.order.infrastructure.primary.order;

import com.hainguyen.ecom.order.domain.order.aggregate.Order;
import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestOrderReadAdmin(UUID publicId,
                                 OrderStatus orderStatus,
                                 List<RestOrderedItemRead> orderedItems,
                                 String address,
                                 String email) {

  public static RestOrderReadAdmin fromDomain(Order order) {
    StringBuilder address = new StringBuilder();
    if(order.getUser().getUserAddress() != null){
      address.append(order.getUser().getUserAddress().street());
      address.append(", ");
      address.append(order.getUser().getUserAddress().city());
      address.append(", ");
      address.append(order.getUser().getUserAddress().zipcode());
      address.append(", ");
      address.append(order.getUser().getUserAddress().country());
    }

    return RestOrderReadAdminBuilder.restOrderReadAdmin()
      .publicId(order.getPublicId().value())
      .orderStatus(order.getStatus())
      .orderedItems(RestOrderedItemRead.fromDomain(order.getOrderedProducts()))
      .address(address.toString())
      .email(order.getUser().getEmail().value())
      .build();
  }
}
