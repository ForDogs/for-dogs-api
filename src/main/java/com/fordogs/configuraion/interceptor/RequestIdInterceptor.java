package com.fordogs.configuraion.interceptor;

import com.fordogs.core.util.constants.HeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader(HeaderConstants.REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        response.addHeader(HeaderConstants.REQUEST_ID_HEADER, requestId);
        request.setAttribute(HeaderConstants.REQUEST_ID_HEADER, requestId);

        return true;
    }
}
