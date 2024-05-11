package com.fordogs.core.domian.vo;

import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedPassword extends WrapperObject<String> {

    @Builder
    public EncryptedPassword(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateBCryptEncoding(value)) {
            throw new IllegalArgumentException("비밀번호가 BCrypt 인코딩 값이 아닙니다.");
        }
    }
}
