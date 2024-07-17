package com.fordogs.cart.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "cart")
public class CartEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    })
    private Quantity quantity;

    private boolean canceled = false;

    @Builder
    public CartEntity(ProductEntity product, UserEntity user, Quantity quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.canceled = false;
    }

    public void updateQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public void cancel() {
        this.canceled = true;
    }
}
