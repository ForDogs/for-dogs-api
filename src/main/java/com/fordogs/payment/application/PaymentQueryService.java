package com.fordogs.payment.application;

import com.fordogs.payment.domain.entity.PaymentEntity;
import com.fordogs.payment.error.PaymentErrorCode;
import com.fordogs.payment.infrastructure.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentEntity findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(PaymentErrorCode.PAYMENT_NOT_FOUND::toException);
    }
}
