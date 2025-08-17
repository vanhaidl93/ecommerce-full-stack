package com.hainguyen.ecom.order.infrastructure.secondary.service.stripe;

import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.hainguyen.ecom.order.domain.order.exception.CartPaymentException;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeService {

  @Value("${application.stripe.api-key}")
  private String apiKey;

  @Value("${application.client-base-url}")
  private String clientBaseUrl;

  public StripeService() {
  }

  @PostConstruct
  public void setApiKey() {
    Stripe.apiKey = apiKey;
  }

  public StripeSessionId createPayment(List<Product> products,
                                       List<DetailCartItemRequest> detailCartItemRequests,
                                       User connectedUser) {

    var sessionBuilder= SessionCreateParams.builder()
      .setMode(SessionCreateParams.Mode.PAYMENT)
      .putMetadata("user_public_id",connectedUser.getUserPublicId().value().toString())
      .setCustomerEmail(connectedUser.getEmail().value())
      .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
      .setSuccessUrl(this.clientBaseUrl+"/cart/success?session_id={CHECKOUT_SESSION_ID}")
      .setCancelUrl(this.clientBaseUrl+"/cart/failure");

    // map detailCartItemRequest to Product domain, base on type of cart.
    detailCartItemRequests.forEach(item -> {
      Product mappedProduct = products.stream()
        .filter(product -> product.getPublicId().value().equals(item.productPublicId().value()))
        .findFirst().orElseThrow();

      var productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
          .putMetadata("product_id",mappedProduct.getPublicId().value().toString())
          .setName(mappedProduct.getName().value())
          .build();

      var priceData = SessionCreateParams.LineItem.PriceData.builder()
        .setProductData(productData)
        .setUnitAmountDecimal(BigDecimal.valueOf(Double.valueOf(mappedProduct.getPrice().value()).longValue()*100))
        .setCurrency("USD")
        .build();

      var lineItem =SessionCreateParams.LineItem.builder()
        .setPriceData(priceData)
        .setQuantity(item.quantity())
        .build();

      sessionBuilder.addLineItem(lineItem);
    });

    return createSession(sessionBuilder.build());
  }

  private StripeSessionId createSession(SessionCreateParams sessionInfo) {
    try{
      Session session = Session.create(sessionInfo);
      return new StripeSessionId(session.getId());

    }catch (StripeException ex) {
      throw new CartPaymentException("Error while creating Stripe session");
    }
  }
}
