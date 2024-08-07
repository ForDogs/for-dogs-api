package com.fordogs.core.util;

import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.constants.HeaderConstants;
import com.fordogs.core.util.constants.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenExtractor {

    public static String extractAccessToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader(HeaderConstants.AUTHORIZATION_HEADER);
            if (bearerToken != null && bearerToken.startsWith(TokenConstants.BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(TokenConstants.BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw GlobalErrorCode.internalServerException("액세스 토큰을 추출하는 중 오류가 발생했습니다.");
        }
    }

    public static String extractAccessToken(String bearerToken) {
        try {
            if (bearerToken != null && bearerToken.startsWith(TokenConstants.BEARER_TOKEN_PREFIX)) {
                return bearerToken.substring(TokenConstants.BEARER_TOKEN_PREFIX.length());
            }
            return null;
        } catch (Exception e) {
            throw GlobalErrorCode.internalServerException("액세스 토큰을 추출하는 중 오류가 발생했습니다.");
        }
    }
}
