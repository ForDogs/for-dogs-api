package com.fordogs.product.presentation.request;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.enums.Category;
import com.fordogs.core.domian.vo.wapper.Description;
import com.fordogs.core.domian.vo.wapper.Price;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "상품 등록 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRegisterRequest {

    @Schema(description = "상품명", requiredMode = Schema.RequiredMode.REQUIRED, example = "강아지 하우스 [SMALL]")
    @NotBlank(message = "상품명을 입력해주세요.")
    private String productName;

    @Schema(description = "상품 가격", requiredMode = Schema.RequiredMode.REQUIRED, example = "19.99")
    @NotNull(message = "상품 가격을 입력해주세요.")
    private BigDecimal productPrice;

    @Schema(description = "상품 수량", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private int productQuantity;

    @Schema(description = "상품 설명", example = "강아지 집 회색 계열입니다(재질 : 극세사)")
    private String productDescription;

    @Schema(description = "상품 이미지", example = "[\"image1.jpg\", \"image2.jpg\"]")
    private String[] productImages;

    @Schema(description = "상품 카테고리", defaultValue = "NONE")
    private Category productCategory;

    public ProductEntity toEntity(UserManagementEntity userManagementEntity) {
        return ProductEntity.builder()
                .name(this.productName)
                .price(Price.builder().value(this.productPrice).build())
                .quantity(this.productQuantity)
                .description(Description.builder().value(this.productDescription).build())
                .images(this.productImages)
                .category(this.productCategory)
                .seller(userManagementEntity)
                .build();
    }
}