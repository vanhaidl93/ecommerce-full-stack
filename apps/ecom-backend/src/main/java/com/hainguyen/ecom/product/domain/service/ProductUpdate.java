package com.hainguyen.ecom.product.domain.service;

import com.hainguyen.ecom.order.domain.order.aggregate.OrderProductQuantity;
import com.hainguyen.ecom.product.domain.repository.ProductRepository;

import java.util.List;

public class ProductUpdate {

  private final ProductRepository productRepository;

  public ProductUpdate(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public void updateNbInStockProduct(List<OrderProductQuantity> orderProductQuantities) {
    orderProductQuantities.forEach(orderProductQuantity -> {
      productRepository.updateNbInStockProduct(orderProductQuantity.productPublicId(),
        orderProductQuantity.orderQuantity().value());
    });
  }

}
