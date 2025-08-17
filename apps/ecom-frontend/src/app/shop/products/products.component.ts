import { Component, effect, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductsFilterComponent } from './products-filter/products-filter.component';
import { injectQueryParams } from 'ngxtension/inject-query-params';
import { ProductsFilter } from '../../admin/model/product.model';
import { UserProductService } from '../../shared/service/user-product.service';
import { ToastService } from '../../shared/toast/toast.service';
import { Router } from '@angular/router';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { ProductCardComponent } from '../product-card/product-card.component';

@Component({
  selector: 'app-products',
  imports: [CommonModule, ProductsFilterComponent, ProductCardComponent],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss',
})
export class ProductsComponent {
  categoryPublicId = injectQueryParams('category');
  size = injectQueryParams('size');
  sort = injectQueryParams('sort');

  userProductService = inject(UserProductService);
  toastService = inject(ToastService);
  router = inject(Router);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: ['createdDate,desc'],
  };

  // initialization value.
  productsFilter: ProductsFilter = {
    size: this.size() ? this.size()! : '',
    sort: [this.sort() ? this.sort()! : ''],
    category: this.categoryPublicId(),
  };

  lastCategoryPublicId = '';

  constructor() {
    effect(() => this.handleFilteredProductsQueryError());
    effect(() => this.handleParametersChange());
  }

  filteredProductsQuery = injectQuery(() => ({
    queryKey: ['products', this.productsFilter],
    queryFn: () =>
      lastValueFrom(
        this.userProductService.filter(this.pageRequest, this.productsFilter)
      ),
  }));

  onProductFilterChange(productsFilter: ProductsFilter) {
    productsFilter.category = this.categoryPublicId(); // fixed category
    this.productsFilter = productsFilter;
    this.pageRequest.sort = productsFilter.sort;

    this.router.navigate(['/products'], {
      queryParams: {
        ...productsFilter,
      },
    });

    this.filteredProductsQuery.refetch();
  }

  private handleFilteredProductsQueryError() {
    if (this.filteredProductsQuery.isError()) {
      this.toastService.show(
        'Error! Failed to load products, please try again',
        'ERROR'
      );
    }
  }

  private handleParametersChange() {
    if (this.categoryPublicId()) {
      if (
        this.lastCategoryPublicId !== this.categoryPublicId() &&
        this.lastCategoryPublicId !== ''
      ) {
        this.productsFilter = {
          category: this.categoryPublicId(),
          size: this.size() ? this.size()! : '',
          sort: [this.sort() ? this.sort()! : ''],
        };

        this.filteredProductsQuery.refetch();
      }
    }

    this.lastCategoryPublicId = this.categoryPublicId()!;
  }
}
