package com.fordogs.core.util.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpTokenExtractor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public static String extractAccessToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
            if (bearerToken != null && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }

    public static String extractAccessToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }
}
