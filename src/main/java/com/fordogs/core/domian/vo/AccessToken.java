package com.fordogs.core.domian.vo;

import com.fordogs.core.util.StringValidator;
import com.fordogs.security.exception.JwtException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken extends WrapperObject<String> {

    public AccessToken(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw new JwtException("AccessToken이 JWT 토큰 형식이 아닙니다.");
        }
    }
}
