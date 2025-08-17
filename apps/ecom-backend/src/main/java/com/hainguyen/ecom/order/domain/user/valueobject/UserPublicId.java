package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

import java.util.UUID;

public record UserPublicId(UUID value) {
  public UserPublicId {
    Assert.notNull("value",value);
  }
}
