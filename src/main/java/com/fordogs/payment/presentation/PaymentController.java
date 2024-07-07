package com.fordogs.payment.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.payment.application.PaymentQueryService;
import com.fordogs.payment.application.PaymentService;
import com.fordogs.payment.error.PaymentErrorCode;
import com.fordogs.payment.presentation.request.PaymentCompleteRequest;
import com.fordogs.payment.presentation.response.PaymentCompleteResponse;
import com.fordogs.payment.presentation.response.PaymentDetailsResponse;
import com.fordogs.security.exception.error.SecurityErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Payment", description = "Payment APIs")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentQueryService paymentQueryService;
    private final PaymentService paymentService;

    @Operation(summary = "결제 저장", operationId = "/payments")
    @ApiErrorCode({PaymentErrorCode.class, SecurityErrorCode.class})
    @PostMapping
    public ResponseEntity<SuccessResponse<PaymentCompleteResponse>> handleCompletePaymentRequest(
            @Valid @RequestBody PaymentCompleteRequest request) {
        PaymentCompleteResponse response = paymentService.completePayment(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "결제 상세 정보 조회", operationId = "/payments/{paymentId}")
    @ApiErrorCode({PaymentErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/{paymentId}")
    public ResponseEntity<SuccessResponse<PaymentDetailsResponse>> handleFindPaymentDetailsRequest(
            @Schema(name = "paymentId", description = "결제 ID", example = "11ef3c55-45b6-5b29-be0f-f7e8ebb47ebb") @PathVariable(name = "paymentId") UUID paymentId) {
        PaymentDetailsResponse response = paymentQueryService.findPaymentDetails(paymentId);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
