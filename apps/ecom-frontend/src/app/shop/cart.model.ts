import { ProductPicture } from '../admin/model/product.model';

export interface CartItemAdd {
  publicId: string;
  quantity: number;
}

export interface Cart {
  products: CartItem[];
}

export interface CartItem {
  publicId: string;
  name: string;
  price: number;
  brand: string;
  picture: ProductPicture;
  quantity: number;
}

export interface StripeSession {
  id: string;
}
