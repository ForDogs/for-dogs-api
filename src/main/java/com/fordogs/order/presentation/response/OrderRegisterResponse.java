package com.fordogs.order.presentation.response;

import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "주문 등록 응답")
@Getter
@Setter
@Builder
public class OrderRegisterResponse {

    @Schema(description = "등록된 주문 ID")
    private String orderId;

    @Schema(description = "등록된 상품 상태")
    private OrderStatus orderStatus;

    @Schema(description = "등록된 주문 총 가격")
    private Integer orderTotalPrice;

    @Schema(description = "등록된 개별 상품 주문 ID 리스트")
    private List<String> orderItemIds;

    public static OrderRegisterResponse toResponse(OrderEntity orderEntity) {
        return OrderRegisterResponse.builder()
                .orderId(orderEntity.getId().toString())
                .orderStatus(orderEntity.getStatus())
                .orderTotalPrice(orderEntity.getTotalPrice().getValue())
                .orderItemIds(orderEntity.getOrderItems().stream()
                        .map(item -> item.getId().toString())
                        .collect(Collectors.toList()))
                .build();
    }
}
