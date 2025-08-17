import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { AdminProductService } from '../../admin-product.service';
import { ToastService } from '../../../shared/toast/toast.service';

import {
  BaseProduct,
  createProductFormContent,
  ProductPicture,
  ProductSizes,
  sizes,
} from '../../model/product.model';
import {
  injectMutation,
  injectQuery,
} from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { NgxControlError } from 'ngxtension/control-error';

@Component({
  selector: 'app-create-product.component',
  imports: [CommonModule, ReactiveFormsModule, NgxControlError],
  templateUrl: './create-product.component.html',
  styleUrl: './create-product.component.scss',
})
export class CreateProductComponent {
  formBuilder = inject(FormBuilder);
  adminProductService = inject(AdminProductService);
  toastService = inject(ToastService);
  router = inject(Router);

  productPictures = new Array<ProductPicture>();

  protected readonly sizes = sizes;

  name = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  description = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  price = new FormControl<number>(0, {
    nonNullable: true,
    validators: [Validators.required],
  });
  size = new FormControl<ProductSizes>('XS', {
    nonNullable: true,
    validators: [Validators.required],
  });
  category = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  brand = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  color = new FormControl<string>('', {
    nonNullable: true,
    validators: [Validators.required],
  });
  featured = new FormControl<boolean>(false, {
    nonNullable: true,
    validators: [Validators.required],
  });
  pictures = new FormControl<Array<ProductPicture>>([], {
    nonNullable: true,
    validators: [Validators.required],
  });
  nbInStock = new FormControl<number>(0, {
    nonNullable: true,
    validators: [Validators.required],
  });

  createForm = this.formBuilder.nonNullable.group<createProductFormContent>({
    brand: this.brand,
    color: this.color,
    description: this.description,
    name: this.name,
    price: this.price,
    size: this.size,
    category: this.category,
    featured: this.featured,
    pictures: this.pictures,
    nbInStock: this.nbInStock,
  });

  loading = false;

  createMutation = injectMutation(() => ({
    mutationFn: (product: BaseProduct) =>
      lastValueFrom(this.adminProductService.createProduct(product)),
    onSettled: () => this.onCreationSettled(),
    onSuccess: () => this.onCreationSuccess(),
    onError: () => this.onCreationError(),
  }));

  create(): void {
    const productToCreate: BaseProduct = {
      brand: this.createForm.getRawValue().brand,
      color: this.createForm.getRawValue().color,
      description: this.createForm.getRawValue().name,
      name: this.createForm.getRawValue().name,
      price: this.createForm.getRawValue().price,
      size: this.createForm.getRawValue().size,
      category: {
        publicId: this.createForm.getRawValue().category.split('+')[0],
        name: this.createForm.getRawValue().category.split('+')[1],
      },
      featured: this.createForm.getRawValue().featured,
      pictures: this.productPictures,
      nbInStock: this.createForm.getRawValue().nbInStock,
    };

    this.loading = true;
    this.createMutation.mutate(productToCreate);
  }

  onUploadNewPicture(target: EventTarget | null): void {
    this.productPictures = [];
    const pictureFileList = this.extractFileFromTarget(target);
    if (pictureFileList !== null) {
      for (let i = 0; i < pictureFileList.length; i++) {
        const picture = pictureFileList.item(i);
        if (picture !== null) {
          const productPicture: ProductPicture = {
            file: picture,
            mimeType: picture.type,
          };
          this.productPictures.push(productPicture);
        }
      }
    }
  }

  categoriesQuery = injectQuery(() => ({
    queryKey: ['categories'],
    queryFn: () => lastValueFrom(this.adminProductService.findAllCategories()),
  }));

  private extractFileFromTarget(target: EventTarget | null): FileList | null {
    const htmlInputTarget = target as HTMLInputElement;
    if (target === null || htmlInputTarget.files === null) {
      return null;
    }
    return htmlInputTarget.files;
  }

  private onCreationSettled() {
    this.loading = false;
  }

  private onCreationSuccess() {
    this.router.navigate(['/admin/products/list']);
    this.toastService.show('Product created', 'SUCCESS');
  }

  private onCreationError() {
    this.toastService.show('Issue when creating product', 'ERROR');
  }
}
