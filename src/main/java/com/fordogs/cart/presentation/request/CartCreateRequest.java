package com.fordogs.cart.presentation.request;

import com.fordogs.cart.domain.entity.CartEntity;
import com.fordogs.core.domain.vo.wapper.Quantity;
import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "장바구니 등록 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartCreateRequest {

    @Schema(description = "상품 아이디", requiredMode = Schema.RequiredMode.REQUIRED, example = "11ef1b1c-8e19-e2d5-a95b-e122cba54608")
    @NotNull(message = "상품 아이디를 입력해주세요.")
    private UUID productId;

    @Schema(description = "상품 수량", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "상품 수량을 입력해주세요.")
    private Integer productQuantity;

    public CartEntity toEntity(UserEntity userEntity, ProductEntity productEntity) {
        return CartEntity.builder()
                .user(userEntity)
                .product(productEntity)
                .quantity(Quantity.builder().value(productQuantity).build())
                .build();
    }
}
