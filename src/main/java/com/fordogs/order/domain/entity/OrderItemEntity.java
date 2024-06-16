package com.fordogs.order.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.product.domain.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_items")
public class OrderItemEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    private Integer quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "unit_price"))
    })
    private Price unitPrice;

    @Builder
    public OrderItemEntity(OrderEntity order, ProductEntity product, Integer quantity, Price unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
