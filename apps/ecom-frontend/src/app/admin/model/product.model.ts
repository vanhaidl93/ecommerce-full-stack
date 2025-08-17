import { FormControl, FormRecord } from '@angular/forms';

export type ProductSizes = 'XS' | 'S' | 'M' | 'L' | 'XL' | 'XXL';
export const sizes: ProductSizes[] = ['XS', 'S', 'M', 'L', 'XL', 'XXL'];

export interface ProductCategory {
  publicId?: string;
  name?: string;
}

export interface ProductPicture {
  file: File;
  mimeType: string;
}

export interface BaseProduct {
  brand: string;
  color: string;
  description: string;
  name: string;
  price: number;
  size: ProductSizes;
  category: ProductCategory;
  featured: boolean;
  pictures: ProductPicture[];
  nbInStock: number;
}

export interface Product extends BaseProduct {
  publicId: string;
}

export type CreateCategoryFormContent = {
  name: FormControl<string>;
};

export type createProductFormContent = {
  brand: FormControl<string>;
  color: FormControl<string>;
  description: FormControl<string>;
  name: FormControl<string>;
  price: FormControl<number>;
  size: FormControl<ProductSizes>;
  category: FormControl<string>;
  featured: FormControl<boolean>;
  pictures: FormControl<ProductPicture[]>;
  nbInStock: FormControl<number>;
};

export interface ProductsFilter {
  size?: string;
  category?: string | null;
  sort: string[];
}

export type FilterProductsFromControl = {
  sort: FormControl<string>;
  size: FormRecord<FormControl<boolean>>;
};

export interface ProductFilterForm {
  size?:
    | {
        [size: string]: boolean; // TS index signature.
      }
    | undefined;
  sort: string;
}
