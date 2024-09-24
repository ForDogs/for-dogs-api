package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.constants.TokenConstants;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.domain.entity.UserEntity;
import com.fordogs.user.domain.vo.TokenMetadata;
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
public class AccessToken extends ValueWrapperObject<String> {

    private TokenMetadata metadata;

    @Builder
    public AccessToken(String value, Long expirationTime) {
        super(value);
        this.metadata = new TokenMetadata(expirationTime);
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw GlobalErrorCode.internalServerException("AccessToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static AccessToken createToken(UserEntity user, String encryptedUUIDToken, Key secretKey, int expirationHours) {
        final String userId = user.getId().toString();
        final String account = user.getAccount().getValue();
        final String role = user.getRole().name();

        if (userId == null && account == null && encryptedUUIDToken == null) {
            throw GlobalErrorCode.internalServerException("AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
        }

        Date now = new Date();
        Date expirationDate = DateUtils.addMinutes(now, expirationHours);
        String jwt = Jwts.builder()
                .setSubject(account)
                .claim(TokenConstants.USER_ID_CLAIM, userId)
                .claim(TokenConstants.ROLE_CLAIM, role)
                .claim(TokenConstants.UUID_TOKEN_CLAIM, encryptedUUIDToken)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();

        return AccessToken.builder()
                .value(jwt)
                .expirationTime(expirationDate.getTime())
                .build();
    }
}
