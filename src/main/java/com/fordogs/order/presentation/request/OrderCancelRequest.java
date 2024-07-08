package com.fordogs.order.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "주문 취소 요청")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancelRequest {

    @Schema(description = "주문 취소 사유", example = "단순변심으로 인한 환불입니다.")
    private String cancelReason;
}
