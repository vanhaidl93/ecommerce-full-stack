package com.hainguyen.ecom.order.infrastructure.primary.order;


import com.hainguyen.ecom.product.domain.aggregate.ProductCart;
import com.hainguyen.ecom.product.infrastructure.primary.RestPicture;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record RestProductCart(
  UUID publicId,
  String name,
  double price,
  String brand,
  RestPicture picture,
  int quantity
) {

  public static RestProductCart from(ProductCart productCart) {
    return RestProductCartBuilder.restProductCart()
      .publicId(productCart.getPublicId().value())
      .name(productCart.getName().value())
      .price(productCart.getPrice().value())
      .brand(productCart.getBrand().value())
      .picture(RestPicture.from(productCart.getPicture()))
      .build();
  }

  public static List<RestProductCart> from(List<ProductCart> productCarts) {
    return productCarts.stream().map(RestProductCart::from)
            .collect(Collectors.toList());
  }
}
