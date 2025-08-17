package com.hainguyen.ecom.product.domain.aggregate;

import com.hainguyen.ecom.product.domain.vo.ProductSize;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;

@Builder
public record FilterQuery(PublicId categoryPublicId,
                          List<ProductSize> sizes) {
}
