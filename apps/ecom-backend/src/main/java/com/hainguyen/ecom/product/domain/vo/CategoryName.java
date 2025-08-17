package com.hainguyen.ecom.product.domain.vo;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record CategoryName(String value) {

  public CategoryName{
    Assert.field("value",value).notNull().minLength(3);
  }
}
