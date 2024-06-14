package com.fordogs.product.presentation.response;

import com.fordogs.product.domain.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "상품 등록 응답")
@Getter
@Setter
@Builder
public class ProductRegisterResponse {

    @Schema(description = "등록된 상품 아이디")
    private String productId;

    @Schema(description = "등록된 상품명")
    private String productName;

    public static ProductRegisterResponse toResponse(ProductEntity productEntity) {
        return ProductRegisterResponse.builder()
                .productId(productEntity.getId().toString())
                .productName(productEntity.getName())
                .build();
    }
}
