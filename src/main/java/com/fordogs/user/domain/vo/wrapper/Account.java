package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.error.UserValidationErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends ValueWrapperObject<String> {

    @Builder
    public Account(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateEnglishNumber(value)) {
            throw UserValidationErrorCode.INVALID_ID_FORMAT.toException();
        }
    }
}
