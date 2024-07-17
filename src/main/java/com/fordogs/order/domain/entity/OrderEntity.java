package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.order.error.OrderErrorCode;
import com.fordogs.payment.domain.entity.PaymentEntity;
import com.fordogs.user.domain.entity.mysql.UserEntity;
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
    private UserEntity buyer;

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
    public OrderEntity(UserEntity buyer, OrderStatus orderStatus, Price totalPrice) {
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

    public void calculateTotal() {
        if (orderItems.isEmpty()) {
           throw GlobalErrorCode.internalServerException("주문 상품 내역이 존재하지 않습니다.");
        }
        this.totalPrice = Price.builder()
                .value(orderItems.stream()
                        .mapToInt(OrderItemEntity::calculateItemTotal)
                        .sum())
                .build();
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
