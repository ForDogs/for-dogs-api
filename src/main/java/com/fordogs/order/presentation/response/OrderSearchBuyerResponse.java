package com.fordogs.order.presentation.response;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "구매자 주문 내역 응답")
@Getter
@Setter
@Builder
public class OrderSearchBuyerResponse {

    @Schema(description = "주문 ID")
    private String orderId;

    @Schema(description = "주문 상태")
    private OrderStatus orderStatus;

    @Schema(description = "주문 총 금액")
    private BigDecimal orderTotalPrice;

    @Schema(description = "주문 아이템 정보")
    private OrderItemResponse[] orderItems;

    public static OrderSearchBuyerResponse toResponse(OrderEntity orderEntity) {
        return OrderSearchBuyerResponse.builder()
                .orderId(orderEntity.getId().toString())
                .orderStatus(orderEntity.getStatus())
                .orderTotalPrice(orderEntity.getTotalPrice().getValue())
                .orderItems(orderEntity.getOrderItems().stream()
                        .map(OrderItemResponse::toResponse)
                        .toArray(OrderItemResponse[]::new))
                .build();
    }
}
