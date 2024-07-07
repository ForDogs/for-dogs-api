package com.fordogs.order.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "주문 취소 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancelRequest {

    @Schema(description = "주문 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11ef1b1c-8e19-e2d5-a95b-e122cba54608")
    @NotNull(message = "주문 ID를 입력해주세요.")
    private UUID orderId;

    @Schema(description = "주문 취소 사유", example = "단순변심으로 인한 환불입니다.")
    private String cancelReason;
}
