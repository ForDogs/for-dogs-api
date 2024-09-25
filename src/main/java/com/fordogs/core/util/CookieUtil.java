package com.fordogs.core.util;

import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.user.domain.vo.wrapper.UUIDToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    @Value("${service.domain}")
    public static String serviceDomain;

    public static String createRefreshTokenCookie(RefreshToken refreshToken) {
        return createCookie(CookieConstants.COOKIE_NAME_REFRESH_TOKEN, refreshToken.getValue(), refreshToken.getMetadata().getExpirationTime());
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
                .sameSite("None")
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
                .sameSite("None")
                .maxAge(0)
                .build()
                .toString();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> classType) {
        return classType.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
