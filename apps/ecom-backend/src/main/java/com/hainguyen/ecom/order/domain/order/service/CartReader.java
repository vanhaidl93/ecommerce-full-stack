package com.hainguyen.ecom.order.domain.order.service;

import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartResponse;
import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartResponseBuilder;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.aggregate.ProductCart;

import java.util.List;
import java.util.stream.Collectors;

public class CartReader {

  public CartReader() {
  }

  public DetailCartResponse getDetail(List<Product> products) {
    List<ProductCart> productCarts = products.stream().map(ProductCart::from).collect(Collectors.toList());

    return DetailCartResponseBuilder.detailCartResponse()
      .productCarts(productCarts)
      .build();
  }
}
