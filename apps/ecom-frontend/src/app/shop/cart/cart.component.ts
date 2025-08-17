import { Component, effect, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../cart.service';
import { ToastService } from '../../shared/toast/toast.service';
import { Oauth2Service } from '../../auth/oauth2.service';
import { CartItem, CartItemAdd, StripeSession } from '../cart.model';
import {
  injectMutation,
  injectQuery,
} from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { RouterLink } from '@angular/router';
import { StripeService } from 'ngx-stripe';

@Component({
  selector: 'app-cart',
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  cartService = inject(CartService);
  toastService = inject(ToastService);
  oauth2Service = inject(Oauth2Service);

  stripeService = inject(StripeService);

  cart: Array<CartItem> = [];

  labelCheckout = 'Login to checkbox';
  action: 'login' | 'checkout' = 'login';

  isInitPaymentSessionloading = false;

  cartQuery = injectQuery(() => ({
    queryKey: ['cart'],
    queryFn: () => lastValueFrom(this.cartService.getCartDetails()),
  }));

  constructor() {
    this.getCartItems();
    this.checkUserLoggedIn();
  }

  ngOnInit(): void {
    this.cartService.addedToCart.subscribe((cartItemAddsFromStorage) =>
      this.updateQuantity(cartItemAddsFromStorage)
    );
  }

  addQuantityToCart(publicId: string) {
    this.cartService.addItemToCart(publicId, 'add');
  }

  removeQuantityToCart(publicId: string, quantity: number) {
    if (quantity > 1) {
      this.cartService.addItemToCart(publicId, 'remove');
    }
  }

  removeItem(publicId: string) {
    const itemToRemoveIndex = this.cart.findIndex(
      (cartItem) => cartItem.publicId === publicId
    );

    if (itemToRemoveIndex > -1) {
      this.cart.splice(itemToRemoveIndex, 1);
    }

    this.cartService.removeFromCart(publicId);
  }

  computeTotal() {
    return this.cart.reduce(
      (acc, cartItem) => acc + cartItem.price * cartItem.quantity,
      0
    );
  }

  private getCartItems() {
    effect(() => {
      if (this.cartQuery.isSuccess()) {
        this.cart = this.cartQuery.data().products;
      }
    });
  }

  private checkUserLoggedIn() {
    // create a subscription for login - change state for checkout.
    effect(() => {
      const connectedUserQuery = this.oauth2Service.connectedUserQuery;

      if (connectedUserQuery?.isError()) {
        this.labelCheckout = 'Login to checkout';
        this.action = 'login';
      } else if (connectedUserQuery?.isSuccess()) {
        this.labelCheckout = 'Checkout';
        this.action = 'checkout';
      }
    });
  }

  // update quantity to this.cart from a new local storage. local storage is root.
  private updateQuantity(cartItemAdds: Array<CartItemAdd>) {
    for (const cartItem of this.cart) {
      const cartItemAddForUpdate = cartItemAdds.find(
        (cartItemAdd) => cartItemAdd.publicId === cartItem.publicId
      );
      if (cartItemAddForUpdate) {
        cartItem.quantity = cartItemAddForUpdate.quantity;
      } else {
        // delete invalid cartItem in this.cart.
        this.cart.slice(this.cart.indexOf(cartItem), 1);
      }
    }
  }

  // Payment

  initPaymentSessionMutation = injectMutation(() => ({
    mutationFn: (cart: Array<CartItemAdd>) =>
      lastValueFrom(this.cartService.initPaymentSession(cart)),
    onSuccess: (stripeSession: StripeSession) =>
      this.onSessionCreateSuccess(stripeSession),
  }));

  checkout() {
    if (this.action === 'login') {
      this.oauth2Service.login();
    } else if (this.action === 'checkout') {
      this.isInitPaymentSessionloading = false;
      const cartItemAdds = this.cart.map((item) => ({
        publicId: item.publicId,
        quantity: item.quantity,
      }));
      // init payment StripeSessionId
      this.initPaymentSessionMutation.mutate(cartItemAdds);
    }
  }

  private onSessionCreateSuccess(stripeSession: StripeSession) {
    this.cartService.storeStripeSessionid(stripeSession.id);

    // client redirect user to Stripe payment page.
    this.stripeService
      .redirectToCheckout({ sessionId: stripeSession.id })
      .subscribe((result) => {
        // the returned error or non result.
        this.isInitPaymentSessionloading = false;
        this.toastService.show(`Order result ${result.error.message}`, 'ERROR');
      });
  }
}
