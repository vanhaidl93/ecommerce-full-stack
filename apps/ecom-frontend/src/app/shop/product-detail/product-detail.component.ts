import { Component, effect, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProductService } from '../../shared/service/user-product.service';
import { Router } from '@angular/router';
import { ToastService } from '../../shared/toast/toast.service';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { interval, lastValueFrom, take } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { ProductCardComponent } from '../product-card/product-card.component';
import { CartService } from '../cart.service';
import { Product } from '../../admin/model/product.model';

import { injectQueryParams } from 'ngxtension/inject-query-params';

@Component({
  selector: 'app-product-detail',
  imports: [CommonModule, FaIconComponent, ProductCardComponent],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss',
})
export class ProductDetailComponent {
  publicId = input.required<string>(); // using input function
  // publicId = injectQueryParams('publicId'); // using ngxtension/inject-params, more readable

  productService = inject(UserProductService);
  router = inject(Router);
  toastService = inject(ToastService);
  cartService = inject(CartService);

  lastPublicId = '';
  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  labelAddToCart = 'Add to cart';
  iconAddToCart = 'shopping-cart';

  constructor() {
    effect(() => this.handlePublicIdChange());
    effect(() => this.handleRelatedProductsQueryError());
    effect(() => this.handleProductQueryError());
  }

  productQuery = injectQuery(() => ({
    queryKey: ['product', this.publicId],
    queryFn: () =>
      lastValueFrom(this.productService.findOneByPublicId(this.publicId())),
  }));

  relatedProductsQuery = injectQuery(() => ({
    queryKey: ['related-products', this.publicId, this.pageRequest],
    queryFn: () =>
      lastValueFrom(
        this.productService.findRelatedProduct(
          this.pageRequest,
          this.publicId()
        )
      ),
  }));

  // Cart-shopping.
  addToCart(productToAdd: Product) {
    this.cartService.addItemToCart(productToAdd.publicId, 'add');
    this.labelAddToCart = 'Added to cart';
    this.iconAddToCart = 'check';

    interval(3000)
      .pipe(take(1))
      .subscribe(() => {
        this.labelAddToCart = 'Add to cart';
        this.iconAddToCart = 'shopping-cart';
      });
  }

  private handlePublicIdChange() {
    if (this.publicId()) {
      if (this.lastPublicId !== this.publicId() && this.lastPublicId !== '') {
        this.relatedProductsQuery.refetch();
        this.productQuery.refetch();
      }
      this.lastPublicId = this.publicId();
    }
  }

  private handleRelatedProductsQueryError() {
    if (this.relatedProductsQuery.isError()) {
      this.toastService.show(
        'Error! Failed to load related product. Please try again.',
        'ERROR'
      );
    }
  }

  private handleProductQueryError() {
    if (this.productQuery.isError()) {
      this.toastService.show(
        'Error! Failed to load product. Please try again.',
        'ERROR'
      );
    }
  }
}
