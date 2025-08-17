package com.hainguyen.ecom.order.infrastructure.primary.order;

import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.hainguyen.ecom.order.domain.order.aggregate.DetailCartItemRequestBuilder;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestCartItemRequest(UUID publicId, long quantity) {

  public static DetailCartItemRequest toDomain(RestCartItemRequest restCartItemRequest){
    return DetailCartItemRequestBuilder.detailCartItemRequest()
      .productPublicId(new PublicId(restCartItemRequest.publicId()))
      .quantity(restCartItemRequest.quantity())
      .build();
  }

  public static RestCartItemRequest fromDomain(DetailCartItemRequest detailCartItemRequest){
    return RestCartItemRequestBuilder.restCartItemRequest()
      .publicId(detailCartItemRequest.productPublicId().value())
      .quantity(detailCartItemRequest.quantity())
      .build();
  }

  public static List<DetailCartItemRequest> toDomain(List<RestCartItemRequest> restCartItemRequests){
    return restCartItemRequests.stream().map(RestCartItemRequest::toDomain).toList();
  }

  public static List<RestCartItemRequest> fromDomain(List<DetailCartItemRequest> detailCartItemRequests){
    return detailCartItemRequests.stream().map(RestCartItemRequest::fromDomain).toList();
  }
}
