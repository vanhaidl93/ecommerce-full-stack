import { Component, effect, inject, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FilterProductsFromControl,
  ProductsFilter,
  ProductFilterForm,
  ProductSizes,
  sizes,
} from '../../../admin/model/product.model';
import {
  FormBuilder,
  FormControl,
  FormRecord,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-products-filter',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './products-filter.component.html',
  styleUrl: './products-filter.component.scss',
})
export class ProductsFilterComponent {
  sort = input<string>('createdDate,desc');
  size = input<string>();

  productFilter = output<ProductsFilter>();

  formBuilder = inject(FormBuilder);

  protected readonly sizes: ProductSizes[] = sizes;

  constructor() {
    effect(() => this.updateSizeFormValue());
    effect(() => this.updateSortFormValue());

    this.formFilterProducts.valueChanges.subscribe(() =>
      this.onFilterChange(this.formFilterProducts.getRawValue())
    );
  }

  formFilterProducts =
    this.formBuilder.nonNullable.group<FilterProductsFromControl>({
      sort: new FormControl<string>(this.sort().split(',')[1], {
        nonNullable: true,
        validators: [Validators.required],
      }),
      size: this.buildSizeFormControl(),
    });

  getSizeFormRecord() {
    return this.formFilterProducts.get('size') as FormRecord<
      FormControl<boolean>
    >;
  }

  private buildSizeFormControl(): FormRecord<FormControl<boolean>> {
    const sizeFormRecord = this.formBuilder.nonNullable.record<
      FormControl<boolean>
    >({});

    for (const size of this.sizes) {
      sizeFormRecord.addControl(
        size,
        new FormControl<boolean>(false, { nonNullable: true })
      );
    }
    return sizeFormRecord;
  }

  private onFilterChange(productFilterForm: Partial<ProductFilterForm>) {
    const productFilter: ProductsFilter = {
      size: '',
      sort: [`createdDate,${productFilterForm.sort}`],
    };

    let sizes: [string, boolean][] = [];

    if (productFilterForm.size !== undefined) {
      sizes = Object.entries(productFilterForm.size);
    }

    for (const [sizekey, sizeValue] of sizes) {
      if (sizeValue) {
        if (productFilter.size?.length === 0) {
          productFilter.size = sizekey;
        } else {
          productFilter.size += `,${sizekey}`;
        }
      }
    }
    //  ProductFilter, {size: 'M,XL',sort: 'createdDate,asc',categoryPublicId: undefine }
    // updated from ProductFilterForm
    this.productFilter.emit(productFilter);
  }

  private updateSizeFormValue() {
    if (this.size()) {
      const sizes = this.size()!.split(',');

      for (const size of sizes) {
        this.getSizeFormRecord()
          .get(size)!
          .setValue(true, { emitEvent: false });
        // doesn't emit valueChanges event in formFilterProducts.
        // only emit on user behavior.
      }
    }
  }

  private updateSortFormValue() {
    if (this.sort()) {
      this.formFilterProducts.controls.sort.setValue(
        this.sort().split(',')[1],
        {
          emitEvent: false,
        }
      );
      // doesn't emit valueChanges event in formFilterProducts.
      // only emit on user behavior.
    }
  }
}
