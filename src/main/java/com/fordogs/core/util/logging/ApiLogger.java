package com.fordogs.core.util.logging;

import com.fordogs.core.presentation.ErrorResponse;
import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.TimeUtil;
import com.fordogs.core.util.constants.HeaderConstants;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.core.util.logging.log.ErrorLog;
import com.fordogs.core.util.logging.log.RequestLog;
import com.fordogs.core.util.logging.log.ResponseLog;
import com.fordogs.core.util.logging.log.repository.ErrorLogRepository;
import com.fordogs.core.util.logging.log.repository.RequestLogRepository;
import com.fordogs.core.util.logging.log.repository.ResponseLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApiLogger {

    private final ErrorLogRepository errorLogRepository;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;

    public void beforeLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        RequestLog.RequestLogBuilder requestLogBuilder = RequestLog.builder()
                .requestId(HttpServletUtil.getRequestAttribute(HeaderConstants.REQUEST_ID_HEADER).toString())
                .url(HttpServletUtil.getRequestUrlAndQuery())
                .method(HttpServletUtil.getHttpMethod())
                .header(HttpServletUtil.getRequestHeaders())
                .createAt(TimeUtil.formatLocalDateTime(LocalDateTime.now()));

        for (Object arg : args) {
            if (arg != null && ClassUtils.getUserClass(arg).getSimpleName().contains("Request")) {
                requestLogBuilder.body(JsonConverter.convertObjectToJson(arg));
            }
        }

        requestLogRepository.insert(requestLogBuilder.build());
    }

    public void afterLog(ResponseEntity<?> response) {
        ResponseLog responseLog = ResponseLog.builder()
                .requestId(HttpServletUtil.getRequestAttribute(HeaderConstants.REQUEST_ID_HEADER).toString())
                .statusCode(response.getStatusCode().value())
                .body(JsonConverter.convertObjectToJson(response.getBody()))
                .header(response.getHeaders().toSingleValueMap())
                .isError(false)
                .createAt(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .build();

        responseLogRepository.insert(responseLog);
    }

    public void failLog(ResponseEntity<ErrorResponse> response) {
        String userId = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        ErrorLog errorLog = ErrorLog.builder()
                .requestId(HttpServletUtil.getRequestAttribute(HeaderConstants.REQUEST_ID_HEADER).toString())
                .userId(userId)
                .path(response.getBody() != null ? response.getBody().getPath() : null)
                .statusCode(response.getStatusCode().value())
                .message(response.getBody() != null ? response.getBody().getError().getMessage() : null)
                .stackTrace(response.getBody() != null ? JsonConverter.convertArrayToJson(response.getBody().getError().getStack()) : null)
                .timeStamp(response.getBody() != null ? response.getBody().getTimeStamp() : null)
                .build();

        errorLogRepository.insert(errorLog);
    }
}
