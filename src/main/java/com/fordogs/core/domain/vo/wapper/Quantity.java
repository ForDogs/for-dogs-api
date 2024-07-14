package com.fordogs.core.domain.vo.wapper;

import com.fordogs.core.exception.error.GlobalErrorCode;
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
        if (value <= 0) {
            throw GlobalErrorCode.INVALID_QUANTITY.toException();
        }
    }
}
