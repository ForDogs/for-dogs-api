package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.vo.TokenMetadata;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
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
public class RefreshToken extends ValueWrapperObject<String> {

    @Transient
    private TokenMetadata metadata;

    @Builder
    public RefreshToken(String value, Long expirationTime) {
        super(value);
        this.metadata = new TokenMetadata(expirationTime);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw GlobalErrorCode.internalServerException("RefreshToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static RefreshToken createToken(UserEntity user, Key secretKey, int expirationDays) {
        final String account = user.getAccount().getValue();

        if (account == null) {
            throw GlobalErrorCode.internalServerException("RefreshToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
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
                .expirationTime(expirationDate.getTime())
                .build();
    }
}
