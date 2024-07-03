package com.fordogs.payment.application.integration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.payment.domain.entity.PaymentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDetailResponse extends PaymentBaseResponse {

    private PaymentResponse response;

    @Getter
    @Setter
    @ToString
    public static class PaymentResponse {

        @JsonProperty("imp_uid")
        private String impUid;

        @JsonProperty("merchant_uid")
        private String merchantUid;

        @JsonProperty("pay_method")
        private String payMethod;

        private String channel;

        @JsonProperty("pg_provider")
        private String pgProvider;

        @JsonProperty("emb_pg_provider")
        private String embPgProvider;

        @JsonProperty("pg_tid")
        private String pgTid;

        @JsonProperty("pg_id")
        private String pgId;

        private Boolean escrow;

        @JsonProperty("apply_num")
        private String applyNum;

        @JsonProperty("bank_code")
        private String bankCode;

        @JsonProperty("bank_name")
        private String bankName;

        @JsonProperty("card_code")
        private String cardCode;

        @JsonProperty("card_name")
        private String cardName;

        @JsonProperty("card_issuer_code")
        private String cardIssuerCode;

        @JsonProperty("card_issuer_name")
        private String cardIssuerName;

        @JsonProperty("card_publisher_code")
        private String cardPublisherCode;

        @JsonProperty("card_publisher_name")
        private String cardPublisherName;

        @JsonProperty("card_quota")
        private Integer cardQuota;

        @JsonProperty("card_number")
        private String cardNumber;

        @JsonProperty("card_type")
        private Integer cardType;

        @JsonProperty("vbank_code")
        private String vbankCode;

        @JsonProperty("vbank_name")
        private String vbankName;

        @JsonProperty("vbank_num")
        private String vbankNum;

        @JsonProperty("vbank_holder")
        private String vbankHolder;

        @JsonProperty("vbank_date")
        private Integer vbankDate;

        @JsonProperty("vbank_issued_at")
        private Integer vbankIssuedAt;

        private String name;

        private BigDecimal amount;

        private String currency;

        @JsonProperty("buyer_name")
        private String buyerName;

        @JsonProperty("buyer_email")
        private String buyerEmail;

        @JsonProperty("buyer_tel")
        private String buyerTel;

        @JsonProperty("buyer_addr")
        private String buyerAddr;

        @JsonProperty("buyer_postcode")
        private String buyerPostcode;

        @JsonProperty("custom_data")
        private String customData;

        @JsonProperty("user_agent")
        private String userAgent;

        private String status;

        @JsonProperty("started_at")
        private Integer startedAt;

        @JsonProperty("paid_at")
        private Integer paidAt;

        @JsonProperty("failed_at")
        private Integer failedAt;

        @JsonProperty("fail_reason")
        private String failReason;

        @JsonProperty("receipt_url")
        private String receiptUrl;

        @JsonProperty("cash_receipt_issued")
        private Boolean cashReceiptIssued;

        @JsonProperty("customer_uid")
        private String customerUid;

        @JsonProperty("customer_uid_usage")
        private String customerUidUsage;
    }

    public PaymentEntity toEntity(OrderEntity order) {
        PaymentResponse response = this.getResponse();

        return PaymentEntity.builder()
                .order(order)
                .impUid(response.getImpUid())
                .payMethod(response.getPayMethod())
                .channel(response.getChannel())
                .pgProvider(response.getPgProvider())
                .embPgProvider(response.getEmbPgProvider())
                .pgTid(response.getPgTid())
                .pgId(response.getPgId())
                .escrow(response.getEscrow() != null && response.getEscrow())
                .applyNum(response.getApplyNum())
                .bankCode(response.getBankCode())
                .bankName(response.getBankName())
                .cardCode(response.getCardCode())
                .cardName(response.getCardName())
                .cardIssuerCode(response.getCardIssuerCode())
                .cardIssuerName(response.getCardIssuerName())
                .cardQuota(response.getCardQuota())
                .cardNumber(response.getCardNumber())
                .cardType(response.getCardType())
                .buyerAddress(response.getBuyerAddr())
                .buyerEmail(response.getBuyerEmail())
                .buyerName(response.getBuyerName())
                .buyerPostcode(response.getBuyerPostcode())
                .buyerTel(response.getBuyerTel())
                .customData(response.getCustomData())
                .status(response.getStatus())
                .name(response.getName())
                .amount(response.getAmount())
                .currency(response.getCurrency())
                .receiptUrl(response.getReceiptUrl())
                .startedAt(response.getStartedAt())
                .paidAt(response.getPaidAt())
                .failedAt(response.getFailedAt())
                .failReason(response.getFailReason())
                .build();
    }
}
