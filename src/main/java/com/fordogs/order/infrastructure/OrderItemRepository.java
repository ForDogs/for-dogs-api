package com.fordogs.order.infrastructure;

import com.fordogs.order.domain.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

    @Query("SELECT oi FROM order_items oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItemEntity> findOrderItemsWithProductsByOrderId(@Param("orderId") UUID orderId);
}
