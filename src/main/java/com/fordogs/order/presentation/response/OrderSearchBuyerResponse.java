package com.fordogs.order.presentation.response;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "구매자 주문 내역 응답")
@Getter
@Setter
@Builder
public class OrderSearchBuyerResponse {

    @Schema(description = "주문 ID")
    private String orderId;

    @Schema(description = "결제 ID")
    private String paymentId;

    @Schema(description = "주문 상태")
    private OrderStatus orderStatus;

    @Schema(description = "주문 총 금액")
    private Integer orderTotalPrice;

    @Schema(description = "주문 날짜")
    private LocalDateTime orderDate;

    @Schema(description = "주문 아이템 정보")
    private List<OrderItemResponse> orderItems;

    public static OrderSearchBuyerResponse toResponse(OrderEntity orderEntity) {
        return OrderSearchBuyerResponse.builder()
                .orderId(orderEntity.getId().toString())
                .paymentId(orderEntity.getPayment() != null ? orderEntity.getPayment().getId().toString() : null)
                .orderStatus(orderEntity.getStatus())
                .orderTotalPrice(orderEntity.getTotalPrice().getValue())
                .orderDate(orderEntity.getCreatedAt())
                .orderItems(orderEntity.getOrderItems().stream()
                        .map(OrderItemResponse::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
