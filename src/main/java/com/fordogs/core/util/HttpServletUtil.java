package com.fordogs.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
        try {
            return Optional
                    .of(getHttpServletRequest())
                    .map(HttpServletRequest::getMethod)
                    .orElse(null);
        } catch (Exception e) {
            throw new IllegalStateException("HTTP 메소드를 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static String getUrlAndQueryString() {
        try {
            return Optional
                    .of(getHttpServletRequest())
                    .map(request -> request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
                    .orElse(null);
        } catch (Exception e) {
            throw new IllegalStateException("URL 및 쿼리 문자열을 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static Map<String, String> requestToHeaderMap() {
        try {
            HttpServletRequest request = getHttpServletRequest();
            Map<String, String> headerMap = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames == null) {
                return new HashMap<>();
            }
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headerMap.put(headerName, headerValue);
            }
            return headerMap;
        } catch (Exception e) {
            throw new IllegalStateException("요청 헤더를 맵으로 변환하는 중 예외가 발생했습니다.", e);
        }
    }

    public static Optional<String> getCookie(String cookieName) {
        try {
            HttpServletRequest request = getHttpServletRequest();
            if (cookieName == null) {
                return Optional.empty();
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        return Optional.ofNullable(cookie.getValue());
                    }
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException("쿠키를 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static Optional<String> getHeader(String headerName) {
        try {
            HttpServletRequest request = getHttpServletRequest();
            if (headerName == null) {
                return Optional.empty();
            }

            String headerValue = request.getHeader(headerName);
            return Optional.ofNullable(headerValue);
        } catch (Exception e) {
            throw new IllegalStateException("요청 헤더를 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static Object getRequestAttribute(String attributeName) {
        HttpServletRequest request = getHttpServletRequest();

        return request.getAttribute(attributeName);
    }

    private static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ignored) {
            throw new IllegalStateException("HTTPServletRequest를 가져오는 도중 예외가 발생했습니다.");
        }
    }
}
