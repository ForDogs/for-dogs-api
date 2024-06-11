package com.fordogs.product.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.product.error.ProductErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price extends ValueWrapperObject<BigDecimal> {

    private static final BigDecimal NEGATIVE_PRICE = BigDecimal.ZERO;

    @Builder
    public Price(BigDecimal value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(BigDecimal value) {
        if (value.compareTo(NEGATIVE_PRICE) < 0) {
            throw ProductErrorCode.INVALID_PRICE.toException();
        }
    }
}
