package com.fordogs.core.util.logging;

import com.fordogs.core.util.HttpServletUtil;
import com.fordogs.core.util.TimeUtil;
import com.fordogs.core.util.constants.HeaderConstants;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.core.util.logging.log.RequestLog;
import com.fordogs.core.util.logging.log.RequestLogRepository;
import com.fordogs.core.util.logging.log.ResponseLog;
import com.fordogs.core.util.logging.log.ResponseLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApiLogger {

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

    public void failLog(ResponseEntity<?> response) {
        ResponseLog responseLog = ResponseLog.builder()
                .requestId(HttpServletUtil.getRequestAttribute(HeaderConstants.REQUEST_ID_HEADER).toString())
                .statusCode(response.getStatusCode().value())
                .body(JsonConverter.convertObjectToJson(response.getBody()))
                .header(response.getHeaders().toSingleValueMap())
                .isError(true)
                .createAt(TimeUtil.formatLocalDateTime(LocalDateTime.now()))
                .build();

        responseLogRepository.insert(responseLog);
    }
}
