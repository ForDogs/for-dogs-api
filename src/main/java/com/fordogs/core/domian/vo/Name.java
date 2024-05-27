package com.fordogs.core.domian.vo;

import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.core.exception.error.UserManagementServiceErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name extends WrapperObject<String> {

    @Builder
    public Name(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateKoreanEnglish(value)) {
            throw UserManagementServiceErrorCode.INVALID_NAME_FORMAT.toException();
        }
    }
}
