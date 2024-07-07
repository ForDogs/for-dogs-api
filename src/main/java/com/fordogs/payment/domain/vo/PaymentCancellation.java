package com.fordogs.payment.domain.vo;

import com.fordogs.core.domain.vo.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancellation extends ValueObject {

    private String cancellationId;

    private Integer cancellationAmount;

    private String cancellationReceiptUrl;

    private String cancellationReason;

    private Integer cancellationAt;
}
