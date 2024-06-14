package com.fordogs.product.presentation.response;

import com.fordogs.product.domain.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "상품 수정 응답")
@Getter
@Setter
@Builder
public class ProductUpdateResponse {

    @Schema(description = "수정된 상품 아이디")
    private String productId;

    @Schema(description = "수정된 상품명")
    private String productName;

    public static ProductUpdateResponse toResponse(ProductEntity productEntity) {
        return ProductUpdateResponse.builder()
                .productId(productEntity.getId().toString())
                .productName(productEntity.getName())
                .build();
    }
}
