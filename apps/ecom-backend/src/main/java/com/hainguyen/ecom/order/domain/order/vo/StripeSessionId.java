package com.hainguyen.ecom.order.domain.order.vo;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record StripeSessionId(String value) {

  public StripeSessionId {
    Assert.notNull("value",value);
  }
}
