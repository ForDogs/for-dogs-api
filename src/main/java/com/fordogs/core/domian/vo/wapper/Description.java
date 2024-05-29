package com.fordogs.core.domian.vo.wapper;

import com.fordogs.core.exception.error.ValueErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

import static com.fordogs.core.util.validator.StringValidator.validateContainsProfanity;
import static com.fordogs.core.util.validator.StringValidator.validateLength;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Description extends ValueWrapperObject<String> {

    private static final String PROFANITY_REGEX = "(새끼|개새끼)";

    private static final int MIN_LENGTH = 0;
    private static final int MAX_LENGTH = 100;

    @Builder
    public Description(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!validateLength(value, MIN_LENGTH, MAX_LENGTH)) {
            throw ValueErrorCode.INVALID_DESCRIPTION_LENGTH.toException();
        }
        if (validateContainsProfanity(value, PROFANITY_REGEX)) {
            throw ValueErrorCode.INVALID_DESCRIPTION_PROFANITY.toException();
        }
    }
}
