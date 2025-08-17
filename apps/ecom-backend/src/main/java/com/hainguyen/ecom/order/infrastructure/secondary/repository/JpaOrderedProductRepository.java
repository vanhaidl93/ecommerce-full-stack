package com.hainguyen.ecom.order.infrastructure.secondary.repository;

import com.hainguyen.ecom.order.infrastructure.secondary.entity.OrderedProductEntity;
import com.hainguyen.ecom.order.infrastructure.secondary.entity.OrderedProductEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderedProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductEntityPK> {
}
