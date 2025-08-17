package com.hainguyen.ecom.order.infrastructure.primary.order;

import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartResponse;
import org.jilt.Builder;

import java.util.List;

@Builder
public record RestDetailCartResponse(
  List<RestProductCart> products
) {

  public static RestDetailCartResponse from(DetailCartResponse detailCartResponse) {
    return RestDetailCartResponseBuilder.restDetailCartResponse()
      .products(RestProductCart.from(detailCartResponse.getProductCarts()))
      .build();
  }
}
