package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record UserFirstname(String value) {

  public UserFirstname{
    Assert.field("value",value).maxLength(255);
  }
}
