package com.fordogs.core.domian.vo;

import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.security.exception.error.JwtErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends WrapperObject<String> {

    @Builder
    public RefreshToken(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw JwtErrorCode.INVALID_REFRESH_TOKEN_FORMAT.toException();
        }
    }
}
