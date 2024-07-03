package com.fordogs.payment.presentation.response;

import com.fordogs.payment.domain.entity.PaymentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "결제 응답")
@Getter
@Setter
@Builder
public class PaymentCompleteResponse {

    @Schema(description = "결제 ID")
    private String paymentId;

    public static PaymentCompleteResponse toResponse(PaymentEntity paymentEntity) {
        return PaymentCompleteResponse.builder()
                .paymentId(paymentEntity.getId().toString())
                .build();
    }
}
