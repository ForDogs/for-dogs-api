package com.fordogs.cart.presentation.response;

import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.product.domain.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "장바구니 전체 조회 응답")
@Getter
@Setter
@Builder
public class CartSearchResponse {

    @Schema(description = "장바구니에 담긴 상품 ID")
    private String cartProductId;

    @Schema(description = "장바구니에 담긴 상품명")
    private String cartProductName;

    @Schema(description = "장바구니에 담긴 상품 가격")
    private Integer cartProductPrice;

    @Schema(description = "장바구니에 담긴 상품 수량")
    private Integer cartProductQuantity;

    @Schema(description = "장바구니에 담긴 상품 이미지")
    private String[] cartProductImages;

    public static CartSearchResponse toResponse(ProductEntity productEntity, Quantity cartProductQuantity) {
        return CartSearchResponse.builder()
                .cartProductId(productEntity.getId().toString())
                .cartProductName(productEntity.getName())
                .cartProductPrice(productEntity.getPrice().getValue())
                .cartProductQuantity(cartProductQuantity.getValue())
                .cartProductImages(JsonConverter.convertJsonToArray(productEntity.getImages()))
                .build();
    }
}
