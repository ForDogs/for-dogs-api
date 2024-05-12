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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
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

    @Getter(AccessLevel.NONE)
    private static final String CLAIMS_ROLE = "role";

    @Getter(AccessLevel.NONE)
    private static final int ACCESS_TOKEN_EXPIRATION_HOURS = 12;

    @Getter(AccessLevel.NONE)
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 14;

    private final UserDetailsService userDetailsService;
    private final TokenProperties tokenProperties;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = getKeyFromBase64EncodedKey(encodeBase64SecretKey());
    }

    private String encodeBase64SecretKey() {
        if (tokenProperties.getSecretKey() == null) {
            throw JwtErrorCode.NO_SECRET_KEY.toException();
        }

        return Encoders.BASE64.encode(tokenProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AccessToken generateAccessToken(UserEntity user) {
        final String userIdentifier = user.getUserIdentifier().getValue();
        final String role = user.getRole().name();

        if (userIdentifier == null) {
            throw JwtErrorCode.NO_USER_DATA_FOR_ACCESS_TOKEN.toException();
        }
        Date now = new Date();
        String jwt = Jwts.builder()
                .setSubject(userIdentifier)
                .claim(CLAIMS_ROLE, role)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addHours(now, ACCESS_TOKEN_EXPIRATION_HOURS))
                .signWith(secretKey)
                .compact();

        return new AccessToken(jwt);
    }

    public RefreshToken generateRefreshToken(UserEntity user) {
        final String userIdentifier = user.getUserIdentifier().getValue();

        if (userIdentifier == null) {
            throw JwtErrorCode.NO_USER_DATA_FOR_REFRESH_TOKEN.toException();
        }
        Date now = new Date();
        String jwt = Jwts.builder()
                .setSubject(userIdentifier)
                .setIssuedAt(now)
                .setExpiration(DateUtils.addDays(now, REFRESH_TOKEN_EXPIRATION_DAYS))
                .signWith(secretKey)
                .compact();

        return new RefreshToken(jwt);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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
