package com.fordogs.security.filter;

import com.fordogs.core.util.HttpTokenExtractor;
import com.fordogs.security.exception.SecurityAuthenticationException;
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

import static com.fordogs.core.util.constants.HttpRequestConstants.REQUEST_ATTRIBUTE_SECURITY_AUTH_EXCEPTION;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = HttpTokenExtractor.extractAccessToken(request);
        if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtUtil.validateToken(accessToken)) {
                    Authentication authentication = jwtUtil.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (SecurityAuthenticationException e) {
                SecurityContextHolder.clearContext();
                request.setAttribute(REQUEST_ATTRIBUTE_SECURITY_AUTH_EXCEPTION, e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
