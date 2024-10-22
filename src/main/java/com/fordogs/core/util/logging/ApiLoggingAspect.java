package com.fordogs.core.util.logging;

import com.fordogs.core.presentation.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect extends ApiLoggingPointCut {

    private final ApiLogger apiLogger;

    // TODO: DynamoDB 과금으로 인한 에러 로깅 중지
    // @Before(value = "apiLoggingPointcut()")
    public void apiBeforeLogging(JoinPoint joinPoint) {
        Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        if (ResponseEntity.class.isAssignableFrom(returnType)) {
            apiLogger.beforeLog(joinPoint);
        }
    }

    // TODO: DynamoDB 과금으로 인한 에러 로깅 중지
    // @AfterReturning(value = "apiLoggingPointcut()", returning = "response")
    public void apiAfterLogging(ResponseEntity<?> response) {
        apiLogger.afterLog(response);
    }

    // TODO: DynamoDB 과금으로 인한 에러 로깅 중지
    // @AfterReturning(value = "within(com.fordogs.core.exception.GlobalExceptionHandler)", returning = "response")
    public void apiThrowException(ResponseEntity<ErrorResponse> response) {
        apiLogger.failLog(response);
    }
}
