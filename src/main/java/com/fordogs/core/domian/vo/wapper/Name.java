package com.fordogs.core.domian.vo.wapper;

import com.fordogs.core.exception.error.ValueErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name extends ValueWrapperObject<String> {

    @Builder
    public Name(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateKoreanEnglish(value)) {
            throw ValueErrorCode.INVALID_NAME_FORMAT.toException();
        }
    }
}
