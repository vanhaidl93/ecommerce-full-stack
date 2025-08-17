package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;

public record AuthorityName(String name) {

  public AuthorityName{
    Assert.field("name",name).notNull();
  }


}
