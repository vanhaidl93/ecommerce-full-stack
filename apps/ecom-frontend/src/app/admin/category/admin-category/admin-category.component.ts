import { Component, effect, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AdminProductService } from '../../admin-product.service';
import { ToastService } from '../../../shared/toast/toast.service';
import {
  injectMutation,
  injectQuery,
  QueryClient,
} from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-admin-category.component',
  imports: [CommonModule, RouterLink, FaIconComponent],
  templateUrl: './admin-category.component.html',
  styleUrl: './admin-category.component.scss',
})
export class AdminCategoryComponent {
  productAdminService = inject(AdminProductService);
  toastService = inject(ToastService);
  queryClient = inject(QueryClient);

  constructor() {
    effect(() => this.handleCategoriesQueryError());
  }

  categoriesQuery = injectQuery(() => ({
    queryKey: ['categories'],
    queryFn: () => {
      return lastValueFrom(this.productAdminService.findAllCategories());
    },
  }));

  deleteMutation = injectMutation(() => ({
    mutationFn: (categoryPublicId: string) =>
      lastValueFrom(this.productAdminService.deleteCategory(categoryPublicId)),
    onSuccess: () => this.onDeletionSuccess(),
    onError: () => this.onDeletionError(),
  }));

  deleteCategory(publicId: string) {
    this.deleteMutation.mutate(publicId);
  }

  private onDeletionSuccess(): void {
    this.queryClient.invalidateQueries({
      queryKey: ['categories'],
    });
    this.toastService.show('Category deleted', 'SUCCESS');
  }

  private onDeletionError(): void {
    this.toastService.show('Issue when deleting category', 'ERROR');
  }

  private handleCategoriesQueryError() {
    if (this.categoriesQuery.isError()) {
      this.toastService.show(
        'Error! Failed to load categoies. Please try again',
        'ERROR'
      );
    }
  }
}
