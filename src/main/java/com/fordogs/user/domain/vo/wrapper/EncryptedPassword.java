package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedPassword extends ValueWrapperObject<String> {

    @Builder
    public EncryptedPassword(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateBCryptEncoding(value)) {
            throw GlobalErrorCode.internalServerException("비밀번호가 BCrypt 인코딩 값이 아닙니다.");
        }
    }
}
