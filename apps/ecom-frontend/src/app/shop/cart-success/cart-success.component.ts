import { afterNextRender, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { injectQueryParams } from 'ngxtension/inject-query-params';
import { CartService } from '../cart.service';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-cart-success',
  imports: [CommonModule, FaIconComponent],
  templateUrl: './cart-success.component.html',
  styleUrl: './cart-success.component.scss',
})
export class CartSuccessComponent {
  // .setSuccessUrl(this.clientBaseUrl+"/cart/success?session_id={CHECKOUT_SESSION_ID}")
  // a component which stripe will forward after payment process successfully
  // as above configuration (from backend)

  stripeSessionId = injectQueryParams('session_id');
  cartService = inject(CartService);

  isValidAccess = true;

  constructor() {
    afterNextRender(() => this.verifySession()); // in client side - browser.
  }

  verifySession() {
    const stripeSessionIdLocalStorage = this.cartService.getStripeSessionId();
    if (stripeSessionIdLocalStorage !== this.stripeSessionId()) {
      this.isValidAccess = false;
    } else {
      this.cartService.deleteStripeSessionId();
      this.cartService.clearCache();
    }
  }
}
