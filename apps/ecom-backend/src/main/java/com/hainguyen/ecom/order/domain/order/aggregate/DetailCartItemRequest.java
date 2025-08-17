package com.hainguyen.ecom.order.domain.order.aggregate;

import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

@Builder
public record DetailCartItemRequest(PublicId productPublicId,long quantity) {
}
