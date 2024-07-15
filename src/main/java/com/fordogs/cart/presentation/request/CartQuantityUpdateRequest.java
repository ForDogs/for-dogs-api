package com.fordogs.cart.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "장바구니 상품 수량 수정 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartQuantityUpdateRequest {

    @Schema(description = "상품 수량", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "상품 수량을 입력해주세요.")
    private Integer productQuantity;
}
