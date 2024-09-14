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
public class Name extends ValueWrapperObject<String> {

    @Builder
    public Name(String value) {
        super(removeWhitespace(value));
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateKoreanEnglishSpecial(value)) {
            throw UserValidationErrorCode.INVALID_NAME_FORMAT.toException();
        }
    }

    private static String removeWhitespace(String value) {
        return value.replaceAll("\\s+", "");
    }
}
