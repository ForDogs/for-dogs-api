package com.fordogs.core.domian.vo;

import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.util.validator.StringValidator;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.security.Key;
import java.util.Date;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken extends WrapperObject<String> {

    @Builder
    public AccessToken(String value) {
        super(value);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw new IllegalArgumentException("AccessToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static AccessToken createToken(UserEntity user, Key secretKey, int expirationHours) {
        final String userIdentifier = user.getUserIdentifier().getValue();
        final String role = user.getRole().name();

        if (userIdentifier == null) {
            throw new IllegalArgumentException("AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
        }

        Date now = new Date();
        String jwt = Jwts.builder()
                .setSubject(userIdentifier)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addHours(now, expirationHours))
                .signWith(secretKey)
                .compact();

        return AccessToken.builder().value(jwt).build();
    }
}
