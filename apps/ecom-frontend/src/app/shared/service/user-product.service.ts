import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  createPaginationOption,
  Page,
  Pagination,
} from '../model/request.model';
import {
  Product,
  ProductCategory,
  ProductsFilter,
} from '../../admin/model/product.model';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserProductService {
  http = inject(HttpClient);

  findAllFeaturedProducts(pageRequest: Pagination) {
    const params = createPaginationOption(pageRequest);
    return this.http.get<Page<Product>>(
      `${environment.apiUrl}/products-shop/featured`,
      { params }
    );
  }

  findOneByPublicId(publicId: string): Observable<Product> {
    return this.http.get<Product>(
      `${environment.apiUrl}/products-shop/find-one`,
      { params: { publicId: publicId } }
    );
  }

  findRelatedProduct(
    pageRequest: Pagination,
    productPublicId: string
  ): Observable<Page<Product>> {
    let params = createPaginationOption(pageRequest);
    params = params.append('publicId', productPublicId);

    return this.http.get<Page<Product>>(
      `${environment.apiUrl}/products-shop/related`,
      { params }
    );
  }

  findAllCategories(): Observable<Page<ProductCategory>> {
    return this.http.get<Page<ProductCategory>>(
      `${environment.apiUrl}/categories`
    );
  }

  filter(
    pageRequest: Pagination,
    productFilter: ProductsFilter
  ): Observable<Page<Product>> {
    let params = createPaginationOption(pageRequest);
    if (productFilter.category) {
      params = params.append('category', productFilter.category);
    }
    if (productFilter.size) {
      params = params.append('productSizes', productFilter.size);
    }
    return this.http.get<Page<Product>>(
      `${environment.apiUrl}/products-shop/filter`,
      { params }
    );
  }
}
