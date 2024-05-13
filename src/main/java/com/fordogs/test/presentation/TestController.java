package com.fordogs.test.presentation;

import com.fordogs.core.presentation.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "Test APIs")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @Operation(summary = "토큰 검증 테스트", operationId = "/test/authorization")
    @GetMapping("/authorization")
    public ResponseEntity<SuccessResponse<String>> handleTestAuthorizationRequest() {
        return new ResponseEntity<>(SuccessResponse.of("Authorization Success"), HttpStatus.OK);
    }

    @Operation(summary = "요청 테스트", operationId = "/test/request")
    @GetMapping("/request")
    public ResponseEntity<SuccessResponse<String>> handleTestRequestRequest() {
        return new ResponseEntity<>(SuccessResponse.of("Request Success"), HttpStatus.OK);
    }
}
