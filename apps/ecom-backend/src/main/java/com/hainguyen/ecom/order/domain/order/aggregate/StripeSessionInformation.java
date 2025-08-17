package com.hainguyen.ecom.order.domain.order.aggregate;

import com.hainguyen.ecom.order.domain.order.vo.StripeSessionId;
import com.hainguyen.ecom.order.domain.user.valueobject.UserAddressToUpdate;
import org.jilt.Builder;

import java.util.List;

@Builder
public record StripeSessionInformation(StripeSessionId stripeSessionId,
                                       UserAddressToUpdate userAddressToUpdate,
                                       List<OrderProductQuantity> orderProductQuantities) {
}
