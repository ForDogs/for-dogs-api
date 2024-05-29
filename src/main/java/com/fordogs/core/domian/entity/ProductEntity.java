package com.fordogs.core.domian.entity;

import com.fordogs.core.domian.enums.Category;
import com.fordogs.core.domian.vo.wapper.Description;
import com.fordogs.core.domian.vo.wapper.Price;
import com.fordogs.core.util.ConverterUtil;
import jakarta.persistence.*;
import lombok.*;

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

    private int quantity;

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
    public ProductEntity(UserManagementEntity seller, String name, Price price, int quantity, Description description, Category category, String[] images) {
        this.seller = seller;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category != null ? category : Category.NONE;
        this.images = ConverterUtil.convertArrayToJson(images);
        this.enabled = true;
    }
}
