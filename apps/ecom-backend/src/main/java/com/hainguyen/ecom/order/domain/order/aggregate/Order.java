package com.hainguyen.ecom.order.domain.order.aggregate;

import com.hainguyen.ecom.order.domain.order.vo.OrderStatus;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public class Order {

  private PublicId publicId;
  private OrderStatus status;
  private User user;
  private List<OrderedProduct> orderedProducts;

  private String stripeId;

  public Order(PublicId publicId, OrderStatus status, User user, List<OrderedProduct> orderedProducts,String stripeId) {
    this.status = status;
    this.user = user;
    this.publicId = publicId;
    this.orderedProducts = orderedProducts;
    this.stripeId = stripeId;
  }

  // Utility methods
  public static Order create(User connectedUser, List<OrderedProduct> orderedProducts, StripeSessionId stripeSessionId) {
    return OrderBuilder.order()
      .publicId(new PublicId(UUID.randomUUID()))
      .status(OrderStatus.PENDING)
      .user(connectedUser)
      .orderedProducts(orderedProducts)
      .stripeId(stripeSessionId.value())
      .build();
  }

  public void validatePayment(){
    this.status = OrderStatus.PAID;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public User getUser() {
    return user;
  }

  public PublicId getPublicId() {
    return publicId;
  }

  public List<OrderedProduct> getOrderedProducts() {
    return orderedProducts;
  }

  public String getStripeId() {
    return stripeId;
  }
}
