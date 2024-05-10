package com.fordogs.security.util;

import com.fordogs.configuraion.properties.TokenProperties;
import com.fordogs.core.domian.entity.UserEntity;
import com.fordogs.core.domian.vo.AccessToken;
import com.fordogs.core.domian.vo.RefreshToken;
import com.fordogs.security.exception.JwtException;
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
            throw new JwtException("토큰 발행을 위한 SecretKey 값이 존재하지 않습니다.");
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
            throw new JwtException("AccessToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
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
            throw new JwtException("RefreshToken 발행을 위한 회원 데이터가 존재하지 않습니다.");
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

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw new JwtException("토큰의 서명이 올바르지 않습니다.", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtException("토큰의 구조가 올바르지 않습니다.", ex);
        } catch (ExpiredJwtException ex) {
            throw new JwtException("토큰의 유효 기한이 만료되었습니다.", ex);
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("지원되지 않는 토큰 형식입니다.", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtException("토큰의 클레임이 비어있거나 유효하지 않습니다.", ex);
        }
    }
}
