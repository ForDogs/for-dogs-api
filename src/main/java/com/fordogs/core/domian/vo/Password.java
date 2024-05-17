package com.fordogs.core.domian.vo;

import com.fordogs.core.exception.error.UserErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password extends WrapperObject<String> {

    @Builder
    public Password(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateLength(value, 10, 16)) {
            throw UserErrorCode.INVALID_PASSWORD_LENGTH.toException();
        }
        if (!StringValidator.validatePasswordPattern(value)) {
            throw UserErrorCode.INVALID_PASSWORD_PATTERN.toException();
        }
        if (!StringValidator.validateNoConsecutiveChars(value)) {
            throw UserErrorCode.INVALID_PASSWORD_CONSECUTIVE_CHARS.toException();
        }
    }
}
