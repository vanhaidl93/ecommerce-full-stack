import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProductService } from '../../shared/service/user-product.service';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { ProductCardComponent } from '../../shop/product-card/product-card.component';

@Component({
  selector: 'app-featured',
  imports: [CommonModule, ProductCardComponent],
  templateUrl: './featured.component.html',
  styleUrl: './featured.component.scss',
})
export class FeaturedComponent {
  userProductService = inject(UserProductService);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  featuredProductQuery = injectQuery(() => ({
    queryKey: ['featured-products'],
    queryFn: () =>
      lastValueFrom(
        this.userProductService.findAllFeaturedProducts(this.pageRequest)
      ),
  }));
}
