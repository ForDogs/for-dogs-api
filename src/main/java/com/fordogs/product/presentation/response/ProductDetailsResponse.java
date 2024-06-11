package com.fordogs.product.presentation.response;

import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.product.domain.enums.Category;
import com.fordogs.core.util.ConverterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "상품 상세 검색 응답")
@Getter
@Setter
@Builder
public class ProductDetailsResponse {

    @Schema(description = "상품 판매자 ID")
    private String productSeller;

    @Schema(description = "상품 ID")
    private String productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 가격")
    private BigDecimal productPrice;

    @Schema(description = "상품 수량")
    private int productQuantity;

    @Schema(description = "상품 설명")
    private String productDescription;

    @Schema(description = "상품 카테고리")
    private Category productCategory;

    @Schema(description = "상품 이미지")
    private String[] productImages;

    public static ProductDetailsResponse toResponse(ProductEntity productEntity) {
        return ProductDetailsResponse.builder()
                .productSeller(productEntity.getSeller().getAccount().getValue())
                .productId(productEntity.getId().toString())
                .productName(productEntity.getName())
                .productPrice(productEntity.getPrice().getValue())
                .productQuantity(productEntity.getQuantity())
                .productDescription(productEntity.getDescription().getValue())
                .productCategory(productEntity.getCategory())
                .productImages(ConverterUtil.convertJsonToArray(productEntity.getImages()))
                .build();
    }
}
