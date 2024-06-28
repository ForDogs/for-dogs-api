package com.fordogs.order.presentation.response;

import com.fordogs.order.domain.entity.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "주문 상품 내역 응답")
@Getter
@Setter
@Builder
public class OrderItemResponse {

    @Schema(description = "개별 주문 상품 ID")
    private String productOrderId;

    @Schema(description = "주문 상품 ID")
    private String orderProductId;

    @Schema(description = "주문 상품명")
    private String orderProductName;

    @Schema(description = "주문 상품 가격")
    private BigDecimal orderProductUnitPrice;

    @Schema(description = "주문 상품 수량")
    private Integer orderProductQuantity;

    public static OrderItemResponse toResponse(OrderItemEntity orderItemEntity) {
        return OrderItemResponse.builder()
                .productOrderId(orderItemEntity.getId().toString())
                .orderProductId(orderItemEntity.getProduct().getId().toString())
                .orderProductName(orderItemEntity.getProduct().getName())
                .orderProductUnitPrice(orderItemEntity.getUnitPrice().getValue())
                .orderProductQuantity(orderItemEntity.getQuantity())
                .build();
    }
}
