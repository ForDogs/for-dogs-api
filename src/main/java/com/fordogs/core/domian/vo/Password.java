package com.fordogs.core.domian.vo;

import com.fordogs.core.util.StringValidator;
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
        if (!StringValidator.validateMinimumLength(value, 9)) {
            throw new IllegalArgumentException("비밀번호는 최소 9자리 이상으로 입력해주세요.");
        }
        if (!StringValidator.validatePasswordPattern(value)) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자를 구성하여 입력해주세요.");
        }
    }
}
