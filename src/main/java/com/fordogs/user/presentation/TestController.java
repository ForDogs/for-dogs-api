package com.fordogs.user.presentation;

import com.fordogs.core.presentation.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<String>> handleTestRequest() {
        return new ResponseEntity<>(SuccessResponse.of("TEST"), HttpStatus.OK);
    }
}
