package com.fordogs.security.provider;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.security.exception.error.JwtErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String CLAIMS_USER_ID = "id";
    private static final int ACCESS_TOKEN_EXPIRATION_HOURS = 12;
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 14;

    private Key secretKey;

    private final UserDetailsService userDetailsService;
    private final TokenProperties tokenProperties;

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

    public AccessToken generateAccessToken(UserEntity user) {
        return AccessToken.createToken(user, secretKey, ACCESS_TOKEN_EXPIRATION_HOURS);
    }

    public RefreshToken generateRefreshToken(UserEntity user) {
        return RefreshToken.createToken(user, secretKey, REFRESH_TOKEN_EXPIRATION_DAYS);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    public boolean compareSubjects(String accessToken, String refreshToken) {
        String accessTokenSubject = extractSubject(accessToken);
        String refreshTokenSubject = extractSubject(refreshToken);

        return Objects.equals(accessTokenSubject, refreshTokenSubject);
    }

    public String extractId(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            return claims.get(CLAIMS_USER_ID, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("요청된 AccessToken 클레임에서 Id를 추출하는 과정에서 예외가 발생하였습니다.", e);
        }
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
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return true;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw JwtErrorCode.INVALID_SIGNATURE.toException();
        } catch (MalformedJwtException ex) {
            throw JwtErrorCode.MALFORMED_TOKEN.toException();
        } catch (ExpiredJwtException ex) {
            throw JwtErrorCode.EXPIRED_TOKEN.toException();
        } catch (UnsupportedJwtException ex) {
            throw JwtErrorCode.UNSUPPORTED_TOKEN.toException();
        } catch (IllegalArgumentException ex) {
            throw JwtErrorCode.INVALID_CLAIMS.toException();
        }
    }
}
