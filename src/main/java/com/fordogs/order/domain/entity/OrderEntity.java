package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.payment.domain.entity.PaymentEntity;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserManagementEntity buyer;

    @OneToOne(mappedBy = "order")
    private PaymentEntity payment;

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

    public void setOrder(PaymentEntity payment) {
        this.payment = payment;
    }

    public void addOrderItems(List<OrderItemEntity> orderItemEntities) {
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItems.add(orderItemEntity);
            orderItemEntity.setOrder(this);
        }
    }

    public void calculateTotalPrice(List<Integer> unitPrices, List<Integer> quantities) {
        if (unitPrices.size() != quantities.size()) {
            throw new IllegalArgumentException("unitPrices와 quantities 리스트의 크기가 일치하지 않습니다.");
        }
        int total = 0;
        for (int i = 0; i < unitPrices.size(); i++) {
            total += unitPrices.get(i) * quantities.get(i);
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
