package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.util.TimeUtil;
import com.fordogs.core.util.constants.TokenClaims;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.domain.entity.UserManagementEntity;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken extends ValueWrapperObject<String> {

    private LocalDateTime expiration = null;

    @Builder
    public AccessToken(String value, LocalDateTime expiration) {
        super(value);
        this.expiration = expiration;
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw new IllegalArgumentException("AccessToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static AccessToken createToken(UserManagementEntity user, Key secretKey, int expirationHours) {
        final String userId = user.getId().toString();
        final String account = user.getAccount().getValue();
        final String role = user.getRole().name();

        if (userId == null && account == null) {
            throw new IllegalArgumentException("AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
        }

        Date now = new Date();
        Date expirationDate = DateUtils.addMinutes(now, expirationHours);
        String jwt = Jwts.builder()
                .setSubject(account)
                .claim(TokenClaims.USER_ID, userId)
                .claim(TokenClaims.ROLE, role)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();

        return AccessToken.builder()
                .value(jwt)
                .expiration(TimeUtil.toLocalDateTime(expirationDate))
                .build();
    }
}
