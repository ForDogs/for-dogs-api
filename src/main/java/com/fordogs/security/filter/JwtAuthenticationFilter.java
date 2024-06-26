package com.fordogs.security.filter;

import com.fordogs.core.util.cookie.CookieUtil;
import com.fordogs.core.util.http.HttpTokenExtractor;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.core.util.constants.RequestAttributeConstants;
import com.fordogs.security.exception.SecurityAuthenticationException;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = HttpTokenExtractor.extractAccessToken(request);
            if (accessToken == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtil.validateToken(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            String uuidToken = CookieUtil.extractCookie(request, CookieConstants.COOKIE_NAME_UUID_TOKEN);
            if (uuidToken == null || uuidToken.isEmpty()) {
                throw SecurityErrorCode.UUID_TOKEN_VALIDATION_FAILED.toException();
            }

            if (!jwtUtil.validateUUIDToken(accessToken, uuidToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SecurityAuthenticationException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(RequestAttributeConstants.REQUEST_ATTRIBUTE_SECURITY_AUTH_EXCEPTION, e);
        }

        filterChain.doFilter(request, response);
    }
}
