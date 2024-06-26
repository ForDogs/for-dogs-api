package com.fordogs.core.util.cookie;

import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    @Value("${service.domain}")
    public static String serviceDomain;

    public static String createRefreshTokenCookie(RefreshToken refreshToken) {
        return createCookie(CookieConstants.COOKIE_NAME_REFRESH_TOKEN, refreshToken.getValue(), refreshToken.getExpirationTime());
    }

    public static String createUUIDTokenCookie(UUIDToken uuidToken, Long expirationTime) {
        return createCookie(CookieConstants.COOKIE_NAME_UUID_TOKEN, uuidToken.getValue(), expirationTime);
    }

    private static String createCookie(String name, String value, Long expirationTime) {
        Instant now = Instant.now();
        Instant expirationInstant = Instant.ofEpochMilli(expirationTime);
        Duration duration = Duration.between(now, expirationInstant);
        long maxAge = duration.getSeconds();

        return ResponseCookie.from(name, value)
                .path("/")
                .domain(serviceDomain)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(maxAge)
                .build()
                .toString();
    }

    public static String createExpiredCookie(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .domain(serviceDomain)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .build()
                .toString();
    }

    public static String extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
