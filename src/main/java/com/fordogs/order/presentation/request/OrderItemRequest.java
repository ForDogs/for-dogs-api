package com.fordogs.order.presentation.request;

import com.fordogs.core.domain.vo.wapper.Price;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.entity.OrderItemEntity;
import com.fordogs.product.domain.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "주문 항목 정보")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemRequest {

    @Schema(description = "주문 상품 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11ef1b1c-8e19-e2d5-a95b-e122cba54608")
    @NotNull(message = "주문 상품 ID를 입력해주세요.")
    private UUID orderProductId;

    @Schema(description = "주문 상품 수량", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "주문 상품 수량을 입력해주세요.")
    private Integer orderQuantity;

    @Schema(description = "주문 당시 상품 단가", requiredMode = Schema.RequiredMode.REQUIRED, example = "19.99")
    @NotNull(message = "주문 당시 상품 단가를 입력해주세요.")
    private BigDecimal orderUnitPrice;

    public OrderItemEntity toEntity(OrderEntity orderEntity, ProductEntity productEntity) {
        return OrderItemEntity.builder()
                .order(orderEntity)
                .product(productEntity)
                .quantity(this.orderQuantity)
                .unitPrice(Price.builder()
                        .value(this.orderUnitPrice)
                        .build())
                .build();
    }
}
