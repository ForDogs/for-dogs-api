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
        return Optional
            .ofNullable(getHttpServletRequest())
            .map(HttpServletRequest::getMethod)
            .orElse(null);
    }

    public static String getUrlAndQueryString() {
        return Optional
            .ofNullable(getHttpServletRequest())
            .map(request -> request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""))
            .orElse(null);
    }

    public static URI createUriWithPostIdFromCurrentRequest(UUID newPostId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{postId}")
            .buildAndExpand(newPostId.toString())
            .toUri();
    }

    public static Map<String, String> requestToHeaderMap() {
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
    }

    public static Optional<String> getCookie(String cookieName) {
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
    }

    private static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ignored) {
            return null;
        }
    }
}
