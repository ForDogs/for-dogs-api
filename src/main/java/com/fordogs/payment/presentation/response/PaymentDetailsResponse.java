package com.fordogs.payment.presentation.response;

import com.fordogs.payment.domain.entity.PaymentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "결제 상세 정보 응답")
@Getter
@Setter
@Builder
public class PaymentDetailsResponse {

    @Schema(description = "결제 ID")
    private String paymentId;

    @Schema(description = "주문 ID")
    private String orderId;

    @Schema(description = "결제 방법")
    private String payMethod;

    @Schema(description = "결제 상태")
    private String status;

    @Schema(description = "결제 금액")
    private Integer amount;

    @Schema(description = "통화")
    private String currency;

    @Schema(description = "결제 날짜")
    private Integer paidAt;

    @Schema(description = "결제 실패 이유")
    private String failReason;

    @Schema(description = "결제 취소 정보")
    private PaymentCancellationDetails cancellationDetails;

    public static PaymentDetailsResponse toResponse(PaymentEntity paymentEntity) {
        PaymentCancellationDetails cancellationDetails = null;
        if (paymentEntity.getPaymentCancellation() != null) {
            cancellationDetails = PaymentCancellationDetails.builder()
                    .cancellationId(paymentEntity.getPaymentCancellation().getCancellationId())
                    .cancellationAmount(paymentEntity.getPaymentCancellation().getCancellationAmount())
                    .cancellationReceiptUrl(paymentEntity.getPaymentCancellation().getCancellationReceiptUrl())
                    .cancellationReason(paymentEntity.getPaymentCancellation().getCancellationReason())
                    .cancellationAt(paymentEntity.getPaymentCancellation().getCancellationAt())
                    .build();
        }

        return PaymentDetailsResponse.builder()
                .paymentId(paymentEntity.getId().toString())
                .orderId(paymentEntity.getOrder().getId().toString())
                .payMethod(paymentEntity.getPayMethod())
                .status(paymentEntity.getStatus())
                .amount(paymentEntity.getAmount())
                .currency(paymentEntity.getCurrency())
                .paidAt(paymentEntity.getPaidAt())
                .failReason(paymentEntity.getFailReason())
                .cancellationDetails(cancellationDetails)
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class PaymentCancellationDetails {
        @Schema(description = "취소 ID")
        private String cancellationId;

        @Schema(description = "취소 금액")
        private Integer cancellationAmount;

        @Schema(description = "취소 영수증 URL")
        private String cancellationReceiptUrl;

        @Schema(description = "취소 사유")
        private String cancellationReason;

        @Schema(description = "취소 날짜")
        private Integer cancellationAt;
    }
}
