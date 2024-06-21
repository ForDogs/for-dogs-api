package com.fordogs.security.util;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.core.util.constants.TokenClaims;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.security.authentication.JwtAuthentication;
import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final int ACCESS_TOKEN_EXPIRATION_HOURS = 5;
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 15;

    private final TokenProperties tokenProperties;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = getKeyFromBase64EncodedKey(encodeBase64SecretKey());
    }

    private String encodeBase64SecretKey() {
        if (tokenProperties.getSecretKey() == null) {
            throw new IllegalArgumentException("토큰 발행을 위한 SecretKey 값이 존재하지 않습니다.");
        }

        return Encoders.BASE64.encode(tokenProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessToken generateAccessToken(UserManagementEntity user) {
        return AccessToken.createToken(user, secretKey, ACCESS_TOKEN_EXPIRATION_HOURS);
    }

    public RefreshToken generateRefreshToken(UserManagementEntity user) {
        return RefreshToken.createToken(user, secretKey, REFRESH_TOKEN_EXPIRATION_DAYS);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return JwtAuthentication.builder()
                .account(claims.getSubject())
                .id(claims.get(TokenClaims.USER_ID, String.class))
                .role(claims.get(TokenClaims.ROLE, String.class))
                .build();
    }

    public boolean compareSubjects(String accessToken, String refreshToken) {
        String accessTokenSubject = extractSubject(accessToken);
        String refreshTokenSubject = extractSubject(refreshToken);

        return Objects.equals(accessTokenSubject, refreshTokenSubject);
    }

    private String extractSubject(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();

            return expiration != null && expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (JwtException ex) {
            validateToken(token);
            return false;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw SecurityErrorCode.INVALID_SIGNATURE.toException();
        } catch (MalformedJwtException ex) {
            throw SecurityErrorCode.MALFORMED_TOKEN.toException();
        } catch (ExpiredJwtException ex) {
            throw SecurityErrorCode.EXPIRED_TOKEN.toException();
        } catch (UnsupportedJwtException ex) {
            throw SecurityErrorCode.UNSUPPORTED_TOKEN.toException();
        } catch (IllegalArgumentException ex) {
            throw SecurityErrorCode.INVALID_CLAIMS.toException();
        }
    }
}