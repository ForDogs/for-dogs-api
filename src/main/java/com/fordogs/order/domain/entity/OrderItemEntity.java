package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.product.domain.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_items")
public class OrderItemEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    })
    private Quantity quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "unit_price"))
    })
    private Price unitPrice;

    @Builder
    public OrderItemEntity(OrderEntity order, ProductEntity product, Quantity quantity, Price unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    protected void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Integer calculateItemTotal() {
        return unitPrice.getValue() * quantity.getValue();
    }
}
