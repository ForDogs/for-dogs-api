package com.fordogs.security.filter;

import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.TokenExtractor;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.core.util.constants.HeaderConstants;
import com.fordogs.security.configuration.ApiRouteConstants;
import com.fordogs.security.exception.SecurityAuthenticationException;
import com.fordogs.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!isSwaggerRequest(request)) {
                setRequestId(request, response);
            }

            String accessToken = TokenExtractor.extractAccessToken(request);
            if (accessToken == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtUtil.validateToken(accessToken)) {
                String uuidToken = CookieUtil.extractCookie(request, CookieConstants.COOKIE_NAME_UUID_TOKEN);
                if (!jwtUtil.validateUUIDToken(accessToken, uuidToken)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SecurityAuthenticationException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(SecurityAuthenticationException.getExceptionName(), e);
        }

        filterChain.doFilter(request, response);
    }

    private void setRequestId(HttpServletRequest request, HttpServletResponse response) {
        String requestId = request.getHeader(HeaderConstants.REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        response.addHeader(HeaderConstants.REQUEST_ID_HEADER, requestId);
        request.setAttribute(HeaderConstants.REQUEST_ID_HEADER, requestId);
    }

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (Map.Entry<String, HttpMethod> entry : ApiRouteConstants.SWAGGER_ENDPOINTS.entrySet()) {
            if (pathMatcher.match(entry.getKey(), uri) && request.getMethod().equalsIgnoreCase(entry.getValue().name())) {
                return true;
            }
        }
        return false;
    }
}
