package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.user.domain.entity.UserManagementEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserManagementEntity buyer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PAID;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "total_price"))
    })
    private Price totalPrice;

    @Builder
    public OrderEntity(UserManagementEntity buyer, OrderStatus orderStatus, Price totalPrice) {
        this.buyer = buyer;
        this.status = orderStatus != null ? orderStatus : OrderStatus.PAID;
        this.totalPrice = totalPrice;
    }
}