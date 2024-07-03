package com.fordogs.payment.domain.entity;

import com.fordogs.core.domain.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "payment_cancel")
public class PaymentCancelEntity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private PaymentEntity payment;

    private String cancellationId;

    private BigDecimal cancelAmount;

    private String cancelReceiptUrl;

    private String cancelReason;

    private Integer cancelledAt;
}
