package com.fordogs.payment.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import com.fordogs.order.domain.entity.OrderEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
}
