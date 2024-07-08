package com.fordogs.core.util;

import com.fordogs.core.util.constants.AuthConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenExtractor {

    public static String extractAccessToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
            if (bearerToken != null && bearerToken.startsWith(AuthConstants.BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(AuthConstants.BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }

    public static String extractAccessToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith(AuthConstants.BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(AuthConstants.BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("액세스 토큰을 추출하는 중 오류가 발생했습니다.", e);
        }
    }
}
