package com.fordogs.core.util;

import com.fordogs.core.exception.error.GlobalErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServletUtil {

    public static String getHttpMethod() {
        return getCurrentHttpServletRequest()
                .map(HttpServletRequest::getMethod)
                    .orElseThrow(() -> GlobalErrorCode.internalServerException("HTTP 메소드를 가져오는 중 예외가 발생했습니다."));
    }

    public static String getRequestUrlAndQuery() {
        return getCurrentHttpServletRequest()
                .map(request -> request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
                .orElseThrow(() -> GlobalErrorCode.internalServerException("URL 및 쿼리 문자열을 가져오는 중 예외가 발생했습니다."));
    }

    public static Map<String, String> getRequestHeaders() {
        HttpServletRequest request = getCurrentHttpServletRequest()
                .orElseThrow(() -> GlobalErrorCode.internalServerException("요청 헤더를 가져오는 중 예외가 발생했습니다."));

        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }

        return headerMap;
    }

    public static Optional<String> getCookieValue(String cookieName) {
        if (cookieName == null) {
            return Optional.empty();
        }

        return getCurrentHttpServletRequest()
                .map(request -> {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals(cookieName)) {
                                return cookie.getValue();
                            }
                        }
                    }
                    return null;
                });
    }

    public static Optional<String> getHeaderValue(String headerName) {
        if (headerName == null) {
            return Optional.empty();
        }

        return getCurrentHttpServletRequest()
                .map(request -> request.getHeader(headerName));
    }

    public static Object getRequestAttribute(String attributeName) {
        return getCurrentHttpServletRequest()
                .map(request -> request.getAttribute(attributeName))
                .orElseThrow(() -> GlobalErrorCode.internalServerException("요청 속성을 가져오는 중 예외가 발생했습니다."));
    }

    public static void addHeaderToResponse(String headerName, String headerValue) {
        getCurrentHttpServletResponse()
                .ifPresent(response -> response.addHeader(headerName, headerValue));
    }

    private static Optional<HttpServletRequest> getCurrentHttpServletRequest() {
        try {
            return Optional.of(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }

    private static Optional<HttpServletResponse> getCurrentHttpServletResponse() {
        try {
            return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse());
        } catch (IllegalStateException e) {
            return Optional.empty();
        }
    }
}
