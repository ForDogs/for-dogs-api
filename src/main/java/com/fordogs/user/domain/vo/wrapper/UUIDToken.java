package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.crypto.EncryptionUtil;
import com.fordogs.core.util.UUIDGenerator;
import com.fordogs.core.util.validator.StringValidator;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UUIDToken extends ValueWrapperObject<String> {

    private UUIDToken(String value) {
        super(value);
        validate(value);
    }

    public static UUIDToken generate() {
        return new UUIDToken(UUIDGenerator.generateSequentialUUID().toString());
    }

    public static UUIDToken from(String value) {
        return new UUIDToken(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateUUID(value)) {
            throw GlobalErrorCode.internalServerException("유효한 UUID 형식이 아닙니다.");
        }
    }

    public String toEncryptedString() {
        return EncryptionUtil.encrypt(getValue());
    }

    public static String decryptToken(String encryptedToken) {
        return EncryptionUtil.decrypt(encryptedToken);
    }
}
