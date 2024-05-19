package com.fordogs.core.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderUtil {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static String extractAccessToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(TOKEN_HEADER);
            if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
                return bearerToken.substring(TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }

    public static String extractAccessToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
                return bearerToken.substring(TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }
}
