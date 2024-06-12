package com.fordogs.health.presentation;

import com.fordogs.core.presentation.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health Check", description = "Health Check APIs")
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private static final String AUTH_SUCCESS_MESSAGE = "토큰이 유효합니다.";
    private static final String SERVER_STATUS_MESSAGE = "서버가 정상적으로 작동 중입니다.";

    @Operation(summary = "API 토큰의 유효성 확인", operationId = "/health/token")
    @GetMapping("/token")
    public ResponseEntity<SuccessResponse<String>> handleTokenValidationRequest() {
        return new ResponseEntity<>(SuccessResponse.of(AUTH_SUCCESS_MESSAGE), HttpStatus.OK);
    }

    @Operation(summary = "서버 상태 확인", operationId = "/health/status")
    @GetMapping("/status")
    public ResponseEntity<SuccessResponse<String>> handleServerStatusRequest() {
        return new ResponseEntity<>(SuccessResponse.of(SERVER_STATUS_MESSAGE), HttpStatus.OK);
    }
}
