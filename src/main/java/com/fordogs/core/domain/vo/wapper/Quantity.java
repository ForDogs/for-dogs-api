package com.fordogs.core.domain.vo.wapper;

import com.fordogs.core.exception.error.GlobalDomainErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity extends ValueWrapperObject<Integer> {

    @Builder
    public Quantity(Integer value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(Integer value) {
        if (value < 0) {
            throw GlobalDomainErrorCode.INVALID_QUANTITY_NEGATIVE.toException();
        }
        if (value > 999) {
            throw GlobalDomainErrorCode.INVALID_QUANTITY_EXCEEDS_MAX.toException();
        }
    }
}
