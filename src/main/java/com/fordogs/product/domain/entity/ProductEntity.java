package com.fordogs.product.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.product.domain.vo.wrapper.Description;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "product")
public class ProductEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    private UserManagementEntity seller;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "price"))
    })
    private Price price;

    private Integer quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "description"))
    })
    private Description description;

    @Enumerated(EnumType.STRING)
    private Category category = Category.NONE;

    @Column(columnDefinition = "JSON")
    private String images;

    private boolean enabled = true;

    @Builder
    public ProductEntity(UserManagementEntity seller, String name, Price price, Integer quantity, Description description, Category category, String[] images) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category != null ? category : Category.NONE;
        this.images = JsonConverter.convertArrayToJson(images);
        this.enabled = true;
    }

    public void update(String name, BigDecimal price, Integer quantity, String description, String[] images, Category category) {
        if (name != null) {
            this.name = name;
        }
        if (price != null) {
            this.price = new Price(price);
        }
        if (quantity != null) {
            this.quantity = quantity;
        }
        if (description != null) {
            this.description = new Description(description);
        }
        if (images != null) {
            this.images = JsonConverter.convertArrayToJson(images);
        }
        if (category != null) {
            this.category = category;
        }
    }

    public void disable() {
        if (!this.enabled) {
            throw ProductErrorCode.PRODUCT_DISABLED.toException();
        }
        this.enabled = false;
    }

    public void decreaseQuantity(int quantityToDecrease) {
        if (quantityToDecrease <= 0) {
            throw ProductErrorCode.INVALID_QUANTITY_DECREASE.toException();
        }
        if (this.quantity < quantityToDecrease) {
            throw ProductErrorCode.INSUFFICIENT_STOCK.toException();
        }
        this.quantity -= quantityToDecrease;
    }
}
