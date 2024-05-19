package com.fordogs.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServletUtil {

    public static String getHttpMethod() {
        try {
            return Optional
                    .ofNullable(getHttpServletRequest())
                    .map(HttpServletRequest::getMethod)
                    .orElse(null);
        } catch (Exception e) {
            throw new IllegalStateException("HTTP 메소드를 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static String getUrlAndQueryString() {
        try {
            return Optional
                    .ofNullable(getHttpServletRequest())
                    .map(request -> request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
                    .orElse(null);
        } catch (Exception e) {
            throw new IllegalStateException("URL 및 쿼리 문자열을 가져오는 중 예외가 발생했습니다.", e);
        }
    }

    public static URI createUriWithPostIdFromCurrentRequest(UUID newPostId) {
        try {
            return ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{postId}")
                    .buildAndExpand(newPostId.toString())
                    .toUri();
        } catch (Exception e) {
            throw new IllegalStateException("현재 요청에서 포스트 ID를 사용하여 URI를 생성하는 중 예외가 발생했습니다.", e);
        }
    }

    public static Map<String, String> requestToHeaderMap() {
        try {
            HttpServletRequest request = getHttpServletRequest();
            if (request == null) {
                return new HashMap<>();
            }
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
            if (request == null || cookieName == null) {
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

    private static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ignored) {
            return null;
        }
    }
}
