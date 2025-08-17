package com.hainguyen.ecom.order.domain.user.valueobject;

import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

@Builder
public record UserAddressToUpdate(UserPublicId userPublicId,
                                  UserAddress userAddress) {

  public UserAddressToUpdate {
    Assert.notNull("value",userAddress);
    Assert.notNull("value",userPublicId);
  }
}
