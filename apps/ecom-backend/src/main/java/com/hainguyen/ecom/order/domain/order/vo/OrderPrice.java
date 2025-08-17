package com.hainguyen.ecom.order.domain.order.vo;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record OrderPrice(double value) {

  public OrderPrice{
    Assert.field("value",value).strictlyPositive();
  }
}
