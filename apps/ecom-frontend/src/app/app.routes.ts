import { Route } from '@angular/router';
import { AdminCategoryComponent } from './admin/category/admin-category/admin-category.component';
import { CreateCategoryComponent } from './admin/category/create-category/create-category.component';
import { roleCheckGuard } from './auth/role-check.guard';
import { HomeComponent } from './home/home.component';
import { AdminProductsComponent } from './admin/product/admin-products/admin-products.component';
import { CreateProductComponent } from './admin/product/create-product/create-product.component';
import { ProductDetailComponent } from './shop/product-detail/product-detail.component';
import { ProductsComponent } from './shop/products/products.component';
import { CartComponent } from './shop/cart/cart.component';
import { CartSuccessComponent } from './shop/cart-success/cart-success.component';
import { UserOrdersComponent } from './user/user-order/user-orders.component';
import { AdminOrdersComponent } from './admin/admin-orders/admin-orders.component';

export const appRoutes: Route[] = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'admin/categories/list',
    component: AdminCategoryComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
  {
    path: 'admin/categories/create',
    component: CreateCategoryComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
  {
    path: 'admin/products/list',
    component: AdminProductsComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
  {
    path: 'admin/products/create',
    component: CreateProductComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
  {
    path: 'admin/orders/list',
    component: AdminOrdersComponent,
    canActivate: [roleCheckGuard],
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
  {
    path: 'product/:publicId',
    component: ProductDetailComponent,
  },
  {
    path: 'products',
    component: ProductsComponent,
  },
  {
    path: 'cart',
    component: CartComponent,
  },
  {
    path: 'cart/success',
    component: CartSuccessComponent,
  },
  {
    path: 'users/orders',
    component: UserOrdersComponent,
  },
  {
    path: 'admin/orders',
    component: AdminCategoryComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
    },
  },
];
