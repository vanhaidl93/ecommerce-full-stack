package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record UserLastname(String value) {

  public UserLastname{
    Assert.field("value",value).maxLength(255);
  }
}
