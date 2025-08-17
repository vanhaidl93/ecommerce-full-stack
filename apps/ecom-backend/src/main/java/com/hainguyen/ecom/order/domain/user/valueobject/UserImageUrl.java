package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record UserImageUrl(String value) {

  public UserImageUrl {
    Assert.field("value",value).maxLength(1000);
  }
}
