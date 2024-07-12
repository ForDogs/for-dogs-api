package com.fordogs.core.util.logging;

import org.aspectj.lang.annotation.Pointcut;

public class ApiLoggingPointCut {

    @Pointcut("@annotation(com.fordogs.core.util.logging.ApiLogging)")
    public void apiLoggingPointcut() {}
}
