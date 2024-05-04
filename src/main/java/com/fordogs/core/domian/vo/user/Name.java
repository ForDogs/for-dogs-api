package com.fordogs.core.domian.vo.user;

import com.fordogs.core.domian.vo.WrapperObject;
import com.fordogs.core.util.StringValidator;
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
        if (!StringValidator.validateNonBlank(value)) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        if (!StringValidator.validateKoreanEnglishNumber(value)) {
            throw new IllegalArgumentException("이름은 한글, 영문, 숫자만 입력 가능합니다.");
        }
    }
}
