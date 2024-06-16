package com.fordogs.order.presentation.response;

import com.fordogs.order.domain.entity.OrderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "주문 등록 응답")
@Getter
@Setter
@Builder
public class OrderRegisterResponse {

    @Schema(description = "등록된 주문 아이디")
    private String orderId;

    @Schema(description = "등록된 상품 상태")
    private String status;

    public static OrderRegisterResponse toResponse(OrderEntity orderEntity) {
        return OrderRegisterResponse.builder()
                .orderId(orderEntity.getId().toString())
                .status(orderEntity.getStatus().toString())
                .build();
    }
}
