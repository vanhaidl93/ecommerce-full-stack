package com.hainguyen.ecom.product.infrastructure.primary;

import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.aggregate.CategoryBuilder;
import com.hainguyen.ecom.product.domain.vo.CategoryName;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.util.UUID;

@Builder
public record RestCategory(UUID publicId,
                           String name) {

  public RestCategory{
    Assert.notNull("name",name);
  }

  public static Category to(RestCategory restCategory){
    var builder = CategoryBuilder.category();
    if(restCategory.publicId != null){
      builder.publicId(new PublicId(restCategory.publicId));
    }
    if (restCategory.name != null){
      builder.name(new CategoryName(restCategory.name));
    }

    return builder.build();
  }

  public static RestCategory from (Category category){
    var builder = RestCategoryBuilder.restCategory();
    if (category.getName() != null){
      builder.name(category.getName().value());
    }

    return builder
      .publicId(category.getPublicId().value())
      .build();
  }


}
