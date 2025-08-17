package com.hainguyen.ecom.order.application;

import com.hainguyen.ecom.order.domain.order.aggregate.*;
import com.hainguyen.ecom.order.domain.order.repository.OrderRepository;
import com.hainguyen.ecom.order.domain.order.service.CartReader;
import com.hainguyen.ecom.order.domain.order.service.OrderCreator;
import com.hainguyen.ecom.order.domain.order.service.OrderReader;
import com.hainguyen.ecom.order.domain.order.service.OrderUpdater;
import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.infrastructure.secondary.service.stripe.StripeService;
import com.hainguyen.ecom.product.application.ProductsApplicationService;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApplicationService {

  private final ProductsApplicationService productsApplicationService;
  private final CartReader cartReader;

  private final UsersApplicationService usersApplicationService;
  private final OrderCreator orderCreator;
  private final OrderUpdater orderUpdater;
  private final OrderReader  orderReader;


  public OrderApplicationService(ProductsApplicationService productsApplicationService,
                                 UsersApplicationService usersApplicationService,
                                 OrderRepository orderRepository, StripeService stripeService) {
    this.productsApplicationService = productsApplicationService;
    this.usersApplicationService = usersApplicationService;
    this.orderUpdater = new OrderUpdater(orderRepository);
    this.orderCreator = new OrderCreator(orderRepository, stripeService);
    this.cartReader = new CartReader();
    this.orderReader = new OrderReader(orderRepository);
  }

  @Transactional(readOnly = true)
  public DetailCartResponse getCartDetails(DetailCartRequest request){
    List<PublicId> productPublicIds = request.items().stream().map(DetailCartItemRequest::productPublicId).toList();

    List<Product> products = productsApplicationService.getProductByProductIds(productPublicIds);
    return cartReader.getDetail(products);
  }

  @Transactional
  public StripeSessionId createOrder(List<DetailCartItemRequest> cartItemRequests){
    User connectedUser = usersApplicationService.getAuthenticatedUser();
    List<PublicId> publicIds = cartItemRequests.stream().map(DetailCartItemRequest::productPublicId).toList();
    List<Product> products = productsApplicationService.getProductByProductIds(publicIds);

    return orderCreator.create(products,cartItemRequests,connectedUser);
  }

  @Transactional
  public void updateOrder(StripeSessionInformation stripeSessionInformation){
    // update order status + compute quantity update nbOfStock product
    List<OrderedProduct> orderedProducts = this.orderUpdater.updateOrderFromStripe(stripeSessionInformation);
    List<OrderProductQuantity> orderProductQuantities = this.orderUpdater.computedQuantity(orderedProducts);

    // update nbOfStock product
    this.productsApplicationService.updateNbInStockProduct(orderProductQuantities);

    // update address use (relate to credit cart, after checkout successfully)
    this.usersApplicationService.updateAddress(stripeSessionInformation.userAddressToUpdate());
  }

  @Transactional(readOnly = true)
  public Page<Order> findOrderForConnectedUser(Pageable pageable){
    User authenticatedUser = usersApplicationService.getAuthenticatedUser();

    return orderReader.findAllByUserPublicId(pageable,authenticatedUser.getUserPublicId().value());
  }

  @Transactional(readOnly = true)
  public Page<Order> findOrderForAdmin(Pageable pageable){
    return  orderReader.findAll(pageable);
  }
}
