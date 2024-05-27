package com.fordogs.core.domian.vo;

import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.core.exception.error.UserManagementServiceErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password extends WrapperObject<String> {

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
            throw UserManagementServiceErrorCode.INVALID_PASSWORD_LENGTH.toException();
        }
        if (!StringValidator.validatePasswordPattern(value)) {
            throw UserManagementServiceErrorCode.INVALID_PASSWORD_PATTERN.toException();
        }
        if (!StringValidator.validateNoConsecutiveChars(value)) {
            throw UserManagementServiceErrorCode.INVALID_PASSWORD_CONSECUTIVE_CHARS.toException();
        }
    }
}
