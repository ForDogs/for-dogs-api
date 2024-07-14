package com.fordogs.order.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fordogs.order.domain.entity.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "주문 상품 내역 응답")
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {

    @Schema(description = "개별 주문 상품 ID")
    private String orderItemId;

    @Schema(description = "주문 상품 ID")
    private String orderProductId;

    @Schema(description = "주문 상품명")
    private String orderProductName;

    @Schema(description = "주문 상품 가격")
    private Integer orderProductUnitPrice;

    @Schema(description = "주문 상품 수량")
    private Integer orderProductQuantity;

    public static OrderItemResponse toResponse(OrderItemEntity orderItemEntity) {
        return OrderItemResponse.builder()
                .orderItemId(orderItemEntity.getId().toString())
                .orderProductId(orderItemEntity.getProduct().getId().toString())
                .orderProductName(orderItemEntity.getProduct().getName())
                .orderProductUnitPrice(orderItemEntity.getUnitPrice().getValue())
                .orderProductQuantity(orderItemEntity.getQuantity().getValue())
                .build();
    }
}
