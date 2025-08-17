package com.hainguyen.ecom.order.domain.order.aggregate;

import com.hainguyen.ecom.product.domain.aggregate.ProductCart;
import org.jilt.Builder;

import java.util.List;

@Builder
public class DetailCartResponse {

  List<ProductCart> productCarts;

  public DetailCartResponse(List<ProductCart> productCarts) {
    this.productCarts = productCarts;
  }

  public List<ProductCart> getProductCarts() {
    return productCarts;
  }
}
