package com.fordogs.product.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.enums.Category;
import com.fordogs.core.util.ConverterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ProductListDto {

    @Schema(description = "상품 전체 검색 응답")
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {

        @Schema(description = "상품 ID")
        private String productId;

        @Schema(description = "상품명")
        private String productName;

        @Schema(description = "상품 가격")
        private BigDecimal productPrice;

        @Schema(description = "상품 수량")
        private int productQuantity;

        @Schema(description = "상품 카테고리")
        private Category productCategory;

        @Schema(description = "상품 이미지")
        private String[] productImages;

        public static Response toResponse(ProductEntity productEntity) {
            return Response.builder()
                    .productId(productEntity.getId().toString())
                    .productName(productEntity.getName())
                    .productPrice(productEntity.getPrice().getValue())
                    .productQuantity(productEntity.getQuantity())
                    .productCategory(productEntity.getCategory())
                    .productImages(ConverterUtil.convertJsonToArray(productEntity.getImages()))
                    .build();
        }
    }
}
