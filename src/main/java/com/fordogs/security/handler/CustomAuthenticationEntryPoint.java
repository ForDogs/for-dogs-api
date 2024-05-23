package com.fordogs.security.handler;

import com.fordogs.core.util.constants.RequestAttributesConstants;
import com.fordogs.security.exception.SecurityAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Object exception = request.getAttribute(RequestAttributesConstants.SECURITY_AUTHENTICATION_EXCEPTION);
        if (exception instanceof SecurityAuthenticationException securityAuthenticationException) {
            resolver.resolveException(request, response, null, securityAuthenticationException);
        } else {
            resolver.resolveException(request, response, null, authException);
        }
    }
}
