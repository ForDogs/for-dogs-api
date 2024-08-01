package com.fordogs.product.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.product.domain.vo.wrapper.Description;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "product")
public class ProductEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity seller;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "price"))
    })
    private Price price;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    })
    private Quantity quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "description"))
    })
    private Description description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "JSON")
    private String images;

    private boolean enabled = true;

    @Builder
    public ProductEntity(UserEntity seller, String name, Price price, Quantity quantity, Description description, Category category, String[] images) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.images = JsonConverter.convertArrayToJson(images);
        this.enabled = true;
    }

    public void update(String name, Integer price, Integer quantity, String description, String[] images, Category category) {
        if (name != null) {
            this.name = name;
        }
        if (price != null) {
            this.price = Price.builder().value(price).build();
        }
        if (quantity != null) {
            this.quantity = Quantity.builder().value(quantity).build();
        }
        if (description != null) {
            this.description = Description.builder().value(description).build();
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
        if (this.quantity.getValue() < quantityToDecrease) {
            throw ProductErrorCode.INSUFFICIENT_STOCK.toException();
        }
        this.quantity = Quantity.builder()
                .value(this.quantity.getValue() - quantityToDecrease)
                .build();
    }

    public void increaseQuantity(int quantityToIncrease) {
        this.quantity = Quantity.builder()
                .value(this.quantity.getValue() + quantityToIncrease)
                .build();
    }

    public String[] getImagesAsArray() {
        return JsonConverter.convertJsonToArray(this.images);
    }
}
