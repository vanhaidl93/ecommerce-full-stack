import { HttpClient } from '@angular/common/http';
import { inject, Injectable, PLATFORM_ID } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Cart, CartItemAdd, StripeSession } from './cart.model';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  platformId = inject(PLATFORM_ID);
  http = inject(HttpClient);

  private keyCartStorage = 'cart';
  private keyStripeSessionId = 'stripe-session-id';

  private addedToCart$ = new BehaviorSubject<Array<CartItemAdd>>([]);
  addedToCart = this.addedToCart$.asObservable();

  constructor() {
    const cartItemAddFromLocalStorage = this.getCartFromLocalStorage();
    this.addedToCart$.next(cartItemAddFromLocalStorage);
  }

  addItemToCart(publicId: string, command: 'add' | 'remove'): void {
    if (isPlatformBrowser(this.platformId)) {
      //const itemToAdd: CartItemAdd = { publicId: publicId, quantity: 1 };
      const newCartItemAdd: CartItemAdd = { publicId, quantity: 1 };
      const cartItemAddFromLocalStorage = this.getCartFromLocalStorage();

      if (cartItemAddFromLocalStorage.length !== 0) {
        const productExist = cartItemAddFromLocalStorage.find(
          (item) => item.publicId === publicId
        );
        if (productExist) {
          if (command === 'add') {
            productExist.quantity++;
          } else if (command === 'remove') {
            productExist.quantity--;
          }
        } else {
          cartItemAddFromLocalStorage.push(newCartItemAdd);
        }
      } else {
        cartItemAddFromLocalStorage.push(newCartItemAdd);
      }

      // update localstorage and emit manually an value to subcriber (navbar.component... )
      localStorage.setItem(
        this.keyCartStorage,
        JSON.stringify(cartItemAddFromLocalStorage)
      );
      this.addedToCart$.next(cartItemAddFromLocalStorage);
    }
  }

  removeFromCart(publicId: string): void {
    if (isPlatformBrowser(this.platformId)) {
      const cartItemAddFromLocalStorage = this.getCartFromLocalStorage();
      const productExist = cartItemAddFromLocalStorage.find(
        (item) => item.publicId === publicId
      );
      if (productExist) {
        cartItemAddFromLocalStorage.splice(
          cartItemAddFromLocalStorage.indexOf(productExist),
          1
        );
        // update localstorage and emit manually an value to subcriber (navbar.component... )
        localStorage.setItem(
          this.keyCartStorage,
          JSON.stringify(cartItemAddFromLocalStorage)
        );
        this.addedToCart$.next(cartItemAddFromLocalStorage);
      }
    }
  }

  getCartDetails(): Observable<Cart> {
    const cartItemAddFromLocalStorage = this.getCartFromLocalStorage();
    const productPublicIdsForUrl = cartItemAddFromLocalStorage.reduce(
      (acc, item) => `${acc}${acc.length > 0 ? ',' : ''}${item.publicId}`,
      ''
    );
    //  orders/get-cart-details
    return (
      this.http
        .get<Cart>(`${environment.apiUrl}/orders/get-cart-details`, {
          params: { productIds: productPublicIdsForUrl },
        })
        // send to backend without quantity (in this case, this field is unnecessary to get Item details)
        // update the quantity into item details from Backend
        .pipe(
          map((cart) => this.mapQuantity(cart, cartItemAddFromLocalStorage))
        )
    );
  }

  // stripe - payment service.
  initPaymentSession(cart: Array<CartItemAdd>): Observable<StripeSession> {
    return this.http.post<StripeSession>(
      `${environment.apiUrl}/orders/init-payment`,
      cart
    );
  }

  storeStripeSessionid(stripeSessionId: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.keyStripeSessionId, stripeSessionId);
    }
  }

  getStripeSessionId(): string {
    if (isPlatformBrowser(this.platformId)) {
      const stripeSessionId = localStorage.getItem(this.keyStripeSessionId);
      if (stripeSessionId) {
        return stripeSessionId;
      }
    }
    return '';
  }

  deleteStripeSessionId(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.keyStripeSessionId);
    }
  }

  clearCache() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.keyCartStorage);
      this.addedToCart$.next([]);
    }
  }

  private getCartFromLocalStorage() {
    if (isPlatformBrowser(this.platformId)) {
      const cartProducts = localStorage.getItem(this.keyCartStorage);
      if (cartProducts) {
        return JSON.parse(cartProducts) as CartItemAdd[];
      } else {
        return [];
      }
    } else {
      // server side
      return [];
    }
  }

  private mapQuantity(
    cart: Cart,
    cartItemAddFromLocalStorage: Array<CartItemAdd>
  ) {
    for (const cartItemAdd of cartItemAddFromLocalStorage) {
      const foundCartItem = cart.products.find(
        (cart) => cart.publicId === cartItemAdd.publicId
      );
      if (foundCartItem) {
        foundCartItem.quantity = cartItemAdd.quantity;
      }
    }
    return cart;
  }
}
