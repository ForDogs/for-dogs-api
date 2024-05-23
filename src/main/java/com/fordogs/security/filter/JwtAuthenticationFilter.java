package com.fordogs.security.filter;

import com.fordogs.core.util.HeaderUtil;
import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.security.exception.SecurityAuthenticationException;
import com.fordogs.security.provider.JwtTokenProvider;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = HeaderUtil.extractAccessToken(request);
        if (accessToken != null) {
            try {
                if (jwtTokenProvider.validateToken(accessToken)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute(RequestAttributesConstants.USER_ID, jwtTokenProvider.extractId(accessToken));
                }
            } catch (SecurityAuthenticationException e) {
                SecurityContextHolder.clearContext();
                request.setAttribute(RequestAttributesConstants.SECURITY_AUTHENTICATION_EXCEPTION, e);
                response.sendError(e.getHttpStatus().value(), e.getMessage()); // permitAll()로 설정된 경로에서도 토큰으로 인해 발생한 예외 처리 진행
            }
        }
        filterChain.doFilter(request, response);
    }
}
