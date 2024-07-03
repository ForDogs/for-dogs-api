package com.fordogs.payment.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.payment.application.PaymentService;
import com.fordogs.payment.error.PaymentErrorCode;
import com.fordogs.payment.presentation.request.PaymentCompleteRequest;
import com.fordogs.payment.presentation.response.PaymentCompleteResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment", description = "Payment APIs")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 저장", operationId = "/payments")
    @ApiErrorCode({PaymentErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<PaymentCompleteResponse>> handleSignupUserRequest(
            @Valid @RequestBody PaymentCompleteRequest request) {
        PaymentCompleteResponse response = paymentService.paymentComplete(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }
}
