package com.fordogs.core.domian.vo.user;

import com.fordogs.core.domian.vo.WrapperObject;
import com.fordogs.core.util.EncryptUtil;
import com.fordogs.core.util.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptedPassword extends WrapperObject<String> {

    public EncryptedPassword(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateBase64Encoding(value)) {
            throw new IllegalArgumentException("비밀번호가 Base64 인코딩 값이 아닙니다.");
        }
    }

    public static EncryptedPassword encodePassword(Password password) {
        if (password == null) {
            throw new IllegalArgumentException("비밀번호가 존재하지 않습니다.");
        }

        return new EncryptedPassword(EncryptUtil.encode(password.getValue()));
    }
}
