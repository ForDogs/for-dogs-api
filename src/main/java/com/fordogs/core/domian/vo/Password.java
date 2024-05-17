package com.fordogs.core.domian.vo;

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
        if (!StringValidator.validateNonBlank(value)) {
            throw new IllegalArgumentException("비밀번호는 공백일 수 없습니다.");
        }
        if (!StringValidator.validateLength(value, 10, 16)) {
            throw new IllegalArgumentException("비밀번호는 최소 10자리 이상 최대 16자리 이하로 입력해주세요.");
        }
        if (!StringValidator.validatePasswordPattern(value)) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자를 구성하여 입력해주세요.");
        }
        if (!StringValidator.validateNoConsecutiveChars(value)) {
            throw new IllegalArgumentException("비밀번호는 연속된 문자열(EX: 123, abc 등)이 포함되지 않도록 입력해주세요.");
        }
    }
}
