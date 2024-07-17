package com.fordogs.security.util;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.constants.TokenConstants;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.security.authentication.JwtAuthentication;
import com.fordogs.user.domain.entity.mysql.UserEntity;
import com.fordogs.user.domain.vo.wrapper.AccessToken;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
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

    private final TokenProperties tokenProperties;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = getKeyFromBase64EncodedKey(encodeBase64SecretKey());
    }

    private String encodeBase64SecretKey() {
        if (tokenProperties.getSecretKey() == null) {
            throw GlobalErrorCode.internalServerException("토큰 발행을 위한 SecretKey 값이 존재하지 않습니다.");
        }

        return Encoders.BASE64.encode(tokenProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessToken generateAccessToken(UserEntity user, String encryptedUUIDToken) {
        return AccessToken.createToken(user, encryptedUUIDToken, secretKey, tokenProperties.getAccessTokenExpirationMinutes());
    }

    public RefreshToken generateRefreshToken(UserEntity user) {
        return RefreshToken.createToken(user, secretKey, tokenProperties.getRefreshTokenExpirationDays());
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);

        return JwtAuthentication.builder()
                .account(claims.getSubject())
                .id(claims.get(TokenConstants.USER_ID_CLAIM, String.class))
                .role(claims.get(TokenConstants.ROLE_CLAIM, String.class))
                .build();
    }

    public boolean compareSubjects(String accessToken, String refreshToken) {
        String accessTokenSubject = extractSubject(accessToken);
        String refreshTokenSubject = extractSubject(refreshToken);

        return Objects.equals(accessTokenSubject, refreshTokenSubject);
    }

    private String extractSubject(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();

            return expiration != null && expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (JwtException ex) {
            validateToken(token);
            return false;
        }
    }

    public boolean validateUUIDToken(String accessToken, String providedUUIDToken) {
        Claims claims = extractAllClaims(accessToken);
        String extractedUUIDToken = claims.get(TokenConstants.UUID_TOKEN_CLAIM, String.class);
        String decryptedUUIDToken = UUIDToken.decryptToken(extractedUUIDToken);
        if (!decryptedUUIDToken.equals(providedUUIDToken)) {
            throw SecurityErrorCode.UUID_TOKEN_VALIDATION_FAILED.toException();
        }
        return true;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
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
