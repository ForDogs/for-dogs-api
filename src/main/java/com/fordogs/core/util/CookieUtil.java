package com.fordogs.core.util;

import com.fordogs.user.domain.vo.wrapper.RefreshToken;
import com.fordogs.core.util.constants.HttpResponseConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    public static final String SERVICE_DOMAIN = "fordogs.store";

    public static String createRefreshTokenCookie(RefreshToken refreshToken) {
        long maxAge = (refreshToken.getExpiration() != null) ?
                Duration.between(LocalDateTime.now(), refreshToken.getExpiration()).getSeconds() : -1;

        return ResponseCookie.from(HttpResponseConstants.COOKIE_NAME_REFRESH_TOKEN, refreshToken.getValue())
                .path("/")
                .domain(SERVICE_DOMAIN)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(maxAge)
                .build()
                .toString();
    }
}
