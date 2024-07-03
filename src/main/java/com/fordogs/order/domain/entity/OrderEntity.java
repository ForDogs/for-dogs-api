package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserManagementEntity buyer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.AWAITING_PAYMENT;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "total_price"))
    })
    private Price totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<OrderItemEntity> orderItems = new ArrayList<>();

    @Builder
    public OrderEntity(UserManagementEntity buyer, OrderStatus orderStatus, Price totalPrice) {
        this.buyer = buyer;
        this.status = orderStatus != null ? orderStatus : OrderStatus.AWAITING_PAYMENT;
        this.totalPrice = totalPrice;
    }

    public void addOrderItems(List<OrderItemEntity> orderItemEntities) {
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItems.add(orderItemEntity);
            orderItemEntity.setOrder(this);
        }
    }

    public void calculateTotalPrice(List<BigDecimal> unitPrices, List<Integer> quantities) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < unitPrices.size(); i++) {
            total = total.add(unitPrices.get(i).multiply(BigDecimal.valueOf(quantities.get(i))));
        }
        this.totalPrice = Price.builder().value(total).build();
    }

    public void changeOrderStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.CANCELLED || this.status == OrderStatus.PAYMENT_FAILED) {
            throw OrderErrorCode.ORDER_CANNOT_BE_MODIFIED.toException();
        }
        if ((this.status == OrderStatus.AWAITING_SHIPMENT
                || this.status == OrderStatus.SHIPPED
                || this.status == OrderStatus.DELIVERED)
                && newStatus == OrderStatus.CANCELLED) {
            throw OrderErrorCode.ORDER_CANNOT_BE_CANCELLED.toException();
        }

        this.status = newStatus;
    }
}
