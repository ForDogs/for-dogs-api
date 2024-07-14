package com.fordogs.product.presentation.response;

import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.product.domain.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "상품 전체 검색 응답")
@Getter
@Setter
@Builder
public class ProductSearchResponse {

    @Schema(description = "상품 ID")
    private String productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private Integer productPrice;

    @Schema(description = "상품 수량")
    private Integer productQuantity;

    @Schema(description = "상품 카테고리")
    private Category productCategory;

    @Schema(description = "상품 이미지")
    private String[] productImages;

    public static ProductSearchResponse toResponse(ProductEntity productEntity) {
        return ProductSearchResponse.builder()
                .productId(productEntity.getId().toString())
                .productName(productEntity.getName())
                .productPrice(productEntity.getPrice().getValue())
                .productQuantity(productEntity.getQuantity().getValue())
                .productCategory(productEntity.getCategory())
                .productImages(JsonConverter.convertJsonToArray(productEntity.getImages()))
                .build();
    }
}
