package com.fordogs.product.presentation.response;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "상품 등록 응답")
@Getter
@Setter
@Builder
public class ProductRegisterResponse {

    @Schema(description = "등록된 상품명")
    private String productName;

    @Schema(description = "등로된 상품 카테고리")
    private Category productCategory;

    public static ProductRegisterResponse toResponse(ProductEntity productEntity) {
        return ProductRegisterResponse.builder()
                .productName(productEntity.getName())
                .productCategory(productEntity.getCategory())
                .build();
    }
}
