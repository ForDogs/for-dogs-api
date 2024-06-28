package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private OrderStatus status = OrderStatus.PAID;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "total_price"))
    })
    private Price totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItemEntity> orderItems = new ArrayList<>();

    @Builder
    public OrderEntity(UserManagementEntity buyer, OrderStatus orderStatus, Price totalPrice) {
        this.buyer = buyer;
        this.status = orderStatus != null ? orderStatus : OrderStatus.PAID;
        this.totalPrice = totalPrice;
    }

    public void addOrderItem(List<OrderItemEntity> orderItemEntities) {
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItems.add(orderItemEntity);
            orderItemEntity.setOrder(this);
        }
    }
}
