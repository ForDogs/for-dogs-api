package com.fordogs.payment.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.payment.application.integration.response.PaymentResponse;
import com.fordogs.payment.domain.vo.PaymentCancellation;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "payment")
public class PaymentEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private OrderEntity order;

    @Embedded
    private PaymentCancellation paymentCancellation;

    private String impUid;

    private String payMethod;

    private String channel;

    private String pgProvider;

    private String embPgProvider;

    private String pgTid;

    private String pgId;

    private boolean escrow;

    private String applyNum;

    private String bankCode;

    private String bankName;

    private String cardCode;

    private String cardName;

    private String cardIssuerCode;

    private String cardIssuerName;

    private Integer cardQuota;

    private String cardNumber;

    private Integer cardType;

    private String buyerAddress;

    private String buyerEmail;

    private String buyerName;

    private String buyerPostcode;

    private String buyerTel;

    private String customData;

    private String status;

    private String name;

    private Integer amount;

    private String currency;

    private String receiptUrl;

    private Integer startedAt;

    private Integer paidAt;

    private Integer failedAt;

    private String failReason;

    public void updateFromPaymentResponse(PaymentResponse response) {
        if (response == null || response.getResponse() == null) {
            throw new IllegalArgumentException("PaymentResponse 값이 유효하지 않습니다.");
        }

        PaymentResponse.PaymentResult result = response.getResponse();
        PaymentResponse.PaymentResult.CancelHistory cancelHistory =
                result.getCancelHistory() != null && result.getCancelHistory().length > 0
                        ? result.getCancelHistory()[0]
                        : null;

        this.impUid = result.getImpUid();
        this.payMethod = result.getPayMethod();
        this.channel = result.getChannel();
        this.pgProvider = result.getPgProvider();
        this.embPgProvider = result.getEmbPgProvider();
        this.pgTid = result.getPgTid();
        this.pgId = result.getPgId();
        this.escrow = result.getEscrow();
        this.applyNum = result.getApplyNum();
        this.bankCode = result.getBankCode();
        this.bankName = result.getBankName();
        this.cardCode = result.getCardCode();
        this.cardName = result.getCardName();
        this.cardIssuerCode = result.getCardIssuerCode();
        this.cardIssuerName = result.getCardIssuerName();
        this.cardQuota = result.getCardQuota();
        this.cardNumber = result.getCardNumber();
        this.cardType = result.getCardType();
        this.name = result.getName();
        this.amount = result.getAmount();
        this.currency = result.getCurrency();
        this.buyerName = result.getBuyerName();
        this.buyerEmail = result.getBuyerEmail();
        this.buyerTel = result.getBuyerTel();
        this.buyerAddress = result.getBuyerAddr();
        this.buyerPostcode = result.getBuyerPostcode();
        this.customData = result.getCustomData();
        this.status = result.getStatus();
        this.startedAt = result.getStartedAt();
        this.paidAt = result.getPaidAt();
        this.failedAt = result.getFailedAt();
        this.failReason = result.getFailReason();
        this.receiptUrl = result.getReceiptUrl();

        if (cancelHistory != null) {
            this.paymentCancellation = PaymentCancellation.builder()
                    .cancellationId(cancelHistory.getCancellationId())
                    .cancellationAmount(cancelHistory.getCancellationAmount())
                    .cancellationReason(cancelHistory.getCancellationReason())
                    .cancellationReceiptUrl(cancelHistory.getCancellationReceiptUrl())
                    .cancellationAt(cancelHistory.getCancellationAt())
                    .build();
        }
    }
}
