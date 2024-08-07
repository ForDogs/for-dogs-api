package com.fordogs.order.infrastructure;

import com.fordogs.order.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @EntityGraph(attributePaths = {"payment", "orderItems", "orderItems.product"})
    @Query("SELECT o FROM orders o WHERE o.createdAt BETWEEN :startDate AND :endDate AND o.buyer.id = :buyerId")
    List<OrderEntity> findOrdersByCreatedAtBetweenAndBuyerId(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("buyerId") UUID buyerId);

    @EntityGraph(attributePaths = {"buyer", "payment", "orderItems", "orderItems.product"})
    @Query("SELECT o FROM orders o " +
            "JOIN o.orderItems oi " +
            "JOIN oi.product p " +
            "WHERE p.seller.id = :sellerId " +
            "AND o.createdAt BETWEEN :startDate AND :endDate")
    List<OrderEntity> findOrdersBySellerAndDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("sellerId") UUID sellerId);
}
