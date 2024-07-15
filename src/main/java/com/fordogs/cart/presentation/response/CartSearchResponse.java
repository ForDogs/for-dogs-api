package com.fordogs.cart.presentation.response;

import com.fordogs.cart.domain.entity.CartEntity;
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

    @Schema(description = "장바구니 ID")
    private String cartId;

    @Schema(description = "장바구니 상품 ID")
    private String cartProductId;

    @Schema(description = "장바구니 상품명")
    private String cartProductName;

    @Schema(description = "장바구니 상품 가격")
    private Integer cartProductPrice;

    @Schema(description = "장바구니 상품 수량")
    private Integer cartProductQuantity;

    @Schema(description = "장바구니 상품 이미지")
    private String[] cartProductImages;

    @Schema(description = "현재 상품 구매 가능 여부")
    private boolean isAvailable;

    public static CartSearchResponse toResponse(CartEntity cartEntity) {
        ProductEntity productEntity = cartEntity.getProduct();
        boolean isAvailable = productEntity.isEnabled() && productEntity.getQuantity().getValue() >= cartEntity.getQuantity().getValue();

        return CartSearchResponse.builder()
                .cartId(cartEntity.getId().toString())
                .cartProductId(productEntity.getId().toString())
                .cartProductName(productEntity.getName())
                .cartProductPrice(productEntity.getPrice().getValue())
                .cartProductQuantity(cartEntity.getQuantity().getValue())
                .cartProductImages(JsonConverter.convertJsonToArray(productEntity.getImages()))
                .isAvailable(isAvailable)
                .build();
    }
}
