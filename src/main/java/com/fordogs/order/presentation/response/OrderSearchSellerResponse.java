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

@Schema(description = "판매자 판매 내역 응답")
@Getter
@Setter
@Builder
public class OrderSearchSellerResponse {

    @Schema(description = "구매자 계정 ID")
    private String buyerAccount;

    @Schema(description = "주문 ID")
    private String orderId;

    @Schema(description = "주문 상태")
    private OrderStatus orderStatus;

    @Schema(description = "주문 총 금액")
    private Integer orderTotalPrice;

    @Schema(description = "주문 날짜")
    private LocalDateTime orderDate;

    @Schema(description = "주문 아이템 정보")
    private List<OrderItemResponse> orderItems;

    public static OrderSearchSellerResponse toResponse(OrderEntity orderEntity) {
        return OrderSearchSellerResponse.builder()
                .buyerAccount(orderEntity.getBuyer().getAccount().getValue())
                .orderId(orderEntity.getId().toString())
                .orderStatus(orderEntity.getStatus())
                .orderTotalPrice(orderEntity.getTotalPrice().getValue())
                .orderDate(orderEntity.getCreatedAt())
                .orderItems(orderEntity.getOrderItems().stream()
                        .map(OrderItemResponse::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
