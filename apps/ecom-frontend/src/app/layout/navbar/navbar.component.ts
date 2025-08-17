import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { Oauth2Service } from '../../auth/oauth2.service';
import { RouterLink } from '@angular/router';
import { UserProductService } from '../../shared/service/user-product.service';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { firstValueFrom } from 'rxjs';
import { CartService } from '../../shop/cart.service';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, FaIconComponent, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent implements OnInit {
  // navbar combines to OidcService
  oauth2Service = inject(Oauth2Service);
  userProductService = inject(UserProductService);
  cartService = inject(CartService);

  nbItemsCart = 0;
  connectedUserQuery = this.oauth2Service.connectedUserQuery;

  categoryQuery = injectQuery(() => ({
    queryKey: ['categories'],
    queryFn: () => firstValueFrom(this.userProductService.findAllCategories()),
  }));

  login(): void {
    this.closeDropDownMenu();
    this.oauth2Service.login();
  }

  logout(): void {
    this.closeDropDownMenu();
    this.oauth2Service.logout();
  }

  isConnected(): boolean {
    return (
      // built-in caching and status tracking, CreateQueryResult<ConnectedUser>
      this.connectedUserQuery?.status() === 'success' &&
      this.connectedUserQuery.data()?.email !== this.oauth2Service.notConnected
    );
  }

  closeDropDownMenu() {
    const bodyElement = document.activeElement as HTMLBodyElement;
    if (bodyElement) {
      bodyElement.blur();
    }
  }

  closeMenu(menu: HTMLDetailsElement) {
    // daisyUI - menu component.
    menu.removeAttribute('open');
  }

  ngOnInit(): void {
    this.listenToCart();
  }

  private listenToCart() {
    this.cartService.addedToCart.subscribe((productsInCart) => {
      this.nbItemsCart = productsInCart.reduce(
        (acc, product) => acc + product.quantity,
        0
      );
    });
  }
}
