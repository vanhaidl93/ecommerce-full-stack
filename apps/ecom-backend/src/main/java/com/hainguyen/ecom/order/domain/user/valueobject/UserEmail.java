package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record UserEmail(String value) {

  public UserEmail {
    Assert.field("value",value).maxLength(255);
  }
}
