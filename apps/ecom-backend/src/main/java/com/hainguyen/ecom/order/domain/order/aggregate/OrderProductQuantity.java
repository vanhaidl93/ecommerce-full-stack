package com.hainguyen.ecom.order.domain.order.aggregate;

import com.hainguyen.ecom.order.domain.order.vo.OrderQuantity;
import com.hainguyen.ecom.order.domain.order.vo.ProductPublicId;
import org.jilt.Builder;

@Builder
public record OrderProductQuantity(OrderQuantity orderQuantity,
                                   ProductPublicId productPublicId) {
}
