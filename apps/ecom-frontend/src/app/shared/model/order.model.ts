export type OrderStatus = 'PENDING' | 'PAID';

export interface OrderedItem {
  name: string;
  quantity: number;
  price: number;
}

export interface UserOrderDetail {
  publicId: string;
  orderStatus: OrderStatus;
  orderedItems: OrderedItem[];
}

export interface AdminOrderDetail {
  publicId: string;
  orderStatus: OrderStatus;
  orderedItems: OrderedItem[];
  address: string;
  email: string;
}
