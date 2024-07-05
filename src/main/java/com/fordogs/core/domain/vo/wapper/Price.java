package com.fordogs.core.domain.vo.wapper;

import com.fordogs.core.exception.error.GlobalErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price extends ValueWrapperObject<Integer> {

    private static final int MAX_PRICE = 1_000_000_000;

    @Builder
    public Price(Integer value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(Integer value) {
        if (value < 0) {
            throw GlobalErrorCode.INVALID_PRICE_NEGATIVE.toException();
        }
        if (value > MAX_PRICE) {
            throw GlobalErrorCode.INVALID_PRICE_EXCEEDS_MAX.toException();
        }
    }
}
