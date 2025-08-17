package com.hainguyen.ecom.order.infrastructure.primary.order;


import com.hainguyen.ecom.order.application.OrderApplicationService;
import com.hainguyen.ecom.order.domain.order.aggregate.*;
import com.hainguyen.ecom.order.domain.order.exception.CartPaymentException;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.valueobject.*;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Address;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hainguyen.ecom.product.infrastructure.primary.ProductsAdminResource.ROLE_ADMIN;

@RestController
@RequestMapping("/api/orders")
public class OrderResource {

  private final OrderApplicationService orderApplicationService;

  @Value("${application.stripe.webhook-secret}")
  private String webhookSecret;

  public OrderResource(OrderApplicationService orderApplicationService) {
    this.orderApplicationService = orderApplicationService;
  }

  @GetMapping("/get-cart-details")
  public ResponseEntity<RestDetailCartResponse> getDetails(@RequestParam("productIds") List<UUID> productPublicId) {
    // requestParamS without quantity on productPublicId, set 1 for default.( unnecessary field to get details).
    List<DetailCartItemRequest> cartItemRequests = productPublicId.stream()
      .map(uuid -> new DetailCartItemRequest(new PublicId(uuid), 1))
      .toList();

    DetailCartRequest detailCartRequest = DetailCartRequestBuilder.detailCartRequest()
      .items(cartItemRequests)
      .build();

    DetailCartResponse detailCartResponse = orderApplicationService.getCartDetails(detailCartRequest);

    return ResponseEntity.ok(RestDetailCartResponse.from(detailCartResponse));
  }

  @PostMapping("/init-payment")
  public ResponseEntity<RestStripeSession> initPayment(@RequestBody List<RestCartItemRequest> cartItems) {
    List<DetailCartItemRequest> detailCartItemRequests = RestCartItemRequest.toDomain(cartItems);
    try {
      StripeSessionId stripeSessionId = orderApplicationService.createOrder(detailCartItemRequests);
      return ResponseEntity.ok(RestStripeSession.from(stripeSessionId));
    } catch (CartPaymentException ex) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/webhook")
  public ResponseEntity<Void> webhookStripe(@RequestBody String paymentEvent,
                                            @RequestHeader("Stripe-Signature") String stripeSignature) {

    Event event = null;
    try {
      event = Webhook.constructEvent(paymentEvent, stripeSignature, webhookSecret);
    } catch (SignatureVerificationException ex) {
      ResponseEntity.badRequest().build();
    }

    Optional<StripeObject> rawStripeObjectOpt = event.getDataObjectDeserializer().getObject();
    if (event.getType().equals("checkout.session.completed")) {
      handleCheckoutSessionCompleted(rawStripeObjectOpt.orElseThrow());
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/user")
  public ResponseEntity<Page<RestOrderRead>> getOrdersForConnectedUser(Pageable pageable){
    Page<Order> orders = orderApplicationService.findOrderForConnectedUser(pageable);

    Page<RestOrderRead> restOrderReads = new PageImpl<>(
      orders.getContent().stream().map(RestOrderRead::fromDomain).toList(),
      pageable,
      orders.getTotalElements());

    return ResponseEntity.ok(restOrderReads);
  }

  @GetMapping("/admin")
  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  public ResponseEntity<Page<RestOrderReadAdmin>> getOrdersForAdmin(Pageable pageable){
    Page<Order> orders = orderApplicationService.findOrderForAdmin(pageable);

    Page<RestOrderReadAdmin> restOrderReads = new PageImpl<>(
      orders.getContent().stream().map(RestOrderReadAdmin::fromDomain).toList(),
      pageable,
      orders.getTotalElements());

    return ResponseEntity.ok(restOrderReads);
  }

  private void handleCheckoutSessionCompleted(StripeObject rawStripeObject) {
    if (rawStripeObject instanceof Session session) {
      // extract address from event type.
      Address address = session.getCustomerDetails().getAddress();

      var userAddress = UserAddressBuilder.userAddress()
        .country(address.getCountry())
        .city(address.getCity())
        .zipcode(address.getPostalCode())
        .street(address.getLine1())
        .build();

      var userAddressToUpdate = UserAddressToUpdateBuilder.userAddressToUpdate()
        .userAddress(userAddress)
        .userPublicId(new UserPublicId(UUID.fromString(session.getMetadata().get("user_public_id"))))
        .build();

      var stripeSessionInformation = StripeSessionInformationBuilder.stripeSessionInformation()
        .stripeSessionId(new StripeSessionId(session.getId()))
        .userAddressToUpdate(userAddressToUpdate)
        .build();

      orderApplicationService.updateOrder(stripeSessionInformation);
    }
  }

}
