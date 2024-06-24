package com.fordogs.user.domain.vo.wrapper;

import com.fordogs.core.domain.vo.wapper.ValueWrapperObject;
import com.fordogs.core.util.constants.TokenClaimConstants;
import com.fordogs.core.util.validator.StringValidator;
import com.fordogs.user.domain.entity.mysql.UserManagementEntity;
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

    private Long expirationTime;

    @Builder
    public AccessToken(String value, Long expirationTime) {
        super(value);
        this.expirationTime = expirationTime;
        validate(value);
    }

    @Override
    protected void validate(String value) {
        if (!StringValidator.validateJWTToken(value)) {
            throw new IllegalArgumentException("AccessToken이 JWT 토큰 형식이 아닙니다.");
        }
    }

    public static AccessToken createToken(UserManagementEntity user, String encryptedUUIDToken, Key secretKey, int expirationHours) {
        final String userId = user.getId().toString();
        final String account = user.getAccount().getValue();
        final String role = user.getRole().name();

        if (userId == null && account == null && encryptedUUIDToken == null) {
            throw new IllegalArgumentException("AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
        }

        Date now = new Date();
        Date expirationDate = DateUtils.addMinutes(now, expirationHours);
        String jwt = Jwts.builder()
                .setSubject(account)
                .claim(TokenClaimConstants.USER_ID, userId)
                .claim(TokenClaimConstants.ROLE, role)
                .claim(TokenClaimConstants.UUID_TOKEN, encryptedUUIDToken)
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
