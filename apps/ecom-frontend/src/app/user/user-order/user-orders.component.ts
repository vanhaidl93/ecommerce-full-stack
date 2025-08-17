import { Component, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { OrderService } from '../../shared/service/order.service';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { OrderedItem } from '../../shared/model/order.model';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-user-orders',
  imports: [CommonModule, FaIconComponent],
  templateUrl: './user-orders.component.html',
  styleUrl: './user-orders.component.scss',
})
export class UserOrdersComponent {
  orderService = inject(OrderService);
  platformId = inject(PLATFORM_ID);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  ordersUserQuery = injectQuery(() => ({
    queryKey: ['user-orders', this.pageRequest],
    queryFn: () =>
      lastValueFrom(
        this.orderService.getOrdersForConnectedUser(this.pageRequest)
      ),
  }));

  computeItemsName(items: OrderedItem[]) {
    return items.map((item) => item.name).join(', ');
  }

  computeItemsQuantity(items: OrderedItem[]) {
    return items.reduce((acc, item) => acc + item.quantity, 0);
  }

  computeTotal(items: OrderedItem[]) {
    return items.reduce((acc, item) => acc + item.price * item.quantity, 0);
  }

  checkIfPlatformBrowser() {
    return isPlatformBrowser(this.platformId);
  }
}
