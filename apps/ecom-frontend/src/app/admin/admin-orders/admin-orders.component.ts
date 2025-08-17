import { Component, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Pagination } from '../../shared/model/request.model';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { OrderService } from '../../shared/service/order.service';
import { lastValueFrom } from 'rxjs';
import { OrderedItem } from '../../shared/model/order.model';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-admin-orders',
  imports: [CommonModule, FaIconComponent],
  templateUrl: './admin-orders.component.html',
  styleUrl: './admin-orders.component.scss',
})
export class AdminOrdersComponent {
  orderService = inject(OrderService);
  platformId = inject(PLATFORM_ID);

  pageRequest: Pagination = {
    page: 0,
    size: 20,
    sort: [],
  };

  ordersAdminQuery = injectQuery(() => ({
    queryKey: ['admin-orders', this.pageRequest],
    queryFn: () =>
      lastValueFrom(this.orderService.getOrdersForAdmin(this.pageRequest)),
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
