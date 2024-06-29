package com.fordogs.order.presentation.request;

import com.fordogs.order.domain.eums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "주문 상태 변경 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStatusUpdateRequest {

    @Schema(description = "주문 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "주문 상태를 입력해주세요.")
    private OrderStatus orderStatus;
}
