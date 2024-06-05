package com.fordogs.product.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.core.util.ConverterUtil;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.product.domain.vo.wrapper.Description;
import com.fordogs.product.domain.vo.wrapper.Price;
import com.fordogs.product.error.ProductErrorCode;
import com.fordogs.user.domain.entity.UserManagementEntity;
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
        this.images = ConverterUtil.convertArrayToJson(images);
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
            this.images = ConverterUtil.convertArrayToJson(images);
        }
        if (category != null) {
            this.category = category;
        }
    }

    private void validateProductIsEnabled() {
        if (!this.enabled) {
            throw ProductErrorCode.PRODUCT_DISABLED.toException();
        }
    }

    public void checkIfProductExists(boolean exists) {
        if (exists) {
            throw ProductErrorCode.PRODUCT_ALREADY_EXISTS.toException();
        }
    }
}
