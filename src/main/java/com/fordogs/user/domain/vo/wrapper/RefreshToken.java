package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.core.util.TimeUtil;
import com.fordogs.core.util.validator.StringValidator;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
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
public class RefreshToken extends ValueWrapperObject<String> {

    @Transient
    private LocalDateTime expiration = null;

    @Builder
    public RefreshToken(String value, LocalDateTime expiration) {
        super(value);
        this.expiration = expiration;
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw new IllegalArgumentException("RefreshToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static RefreshToken createToken(UserManagementEntity user, Key secretKey, int expirationDays) {
        final String account = user.getAccount().getValue();

        if (account == null) {
            throw new IllegalArgumentException("RefreshToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
        }

        Date now = new Date();
        Date expirationDate = DateUtils.addMinutes(now, expirationDays);
        String jwt = Jwts.builder()
                .setSubject(account)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();

        return RefreshToken.builder()
                .value(jwt)
                .expiration(TimeUtil.toLocalDateTime(expirationDate))
                .build();
    }
}
