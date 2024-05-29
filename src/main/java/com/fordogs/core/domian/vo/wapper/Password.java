package com.fordogs.core.domian.vo.wapper;

import com.fordogs.core.exception.error.ValueErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password extends ValueWrapperObject<String> {

    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 16;

    @Builder
    public Password(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateLength(value, MIN_LENGTH, MAX_LENGTH)) {
            throw ValueErrorCode.INVALID_PASSWORD_LENGTH.toException();
        }
        if (!StringValidator.validatePasswordPattern(value)) {
            throw ValueErrorCode.INVALID_PASSWORD_PATTERN.toException();
        }
        if (!StringValidator.validateNoConsecutiveChars(value)) {
            throw ValueErrorCode.INVALID_PASSWORD_CONSECUTIVE_CHARS.toException();
        }
    }
}
