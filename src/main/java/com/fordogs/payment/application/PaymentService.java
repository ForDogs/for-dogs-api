package com.fordogs.payment.application;


import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.order.application.OrderQueryService;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.payment.application.integration.client.PaymentApiClient;
import com.fordogs.payment.application.integration.response.PaymentResponse;
import com.fordogs.payment.domain.entity.PaymentEntity;
import com.fordogs.payment.error.PaymentErrorCode;
import com.fordogs.payment.infrastructure.PaymentRepository;
import com.fordogs.payment.presentation.request.PaymentCompleteRequest;
import com.fordogs.payment.presentation.response.PaymentCompleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderQueryService orderQueryService;
    private final PaymentApiClient paymentApiClient;
    private final PaymentQueryService paymentQueryService;
    private final PaymentRepository paymentRepository;

    @Transactional(noRollbackFor = DomainException.class)
    public PaymentCompleteResponse completePayment(PaymentCompleteRequest request) {
        PaymentResponse paymentResponse = paymentApiClient.getPaymentDetail(request.getImpUid());

        OrderEntity orderEntity = orderQueryService.findOrderById(request.getMerchantUid());
        validatePaymentAmount(orderEntity, paymentResponse);

        String paymentStatus = paymentResponse.getResponse().getStatus();
        if (paymentStatus.equals("paid")) {
            PaymentEntity savedPaymentEntity = paymentRepository.save(paymentResponse.toEntity(orderEntity));
            orderEntity.changeOrderStatus(OrderStatus.PAID);

            return PaymentCompleteResponse.toResponse(savedPaymentEntity);
        }
        if (paymentStatus.equals("ready")) {
            orderEntity.changeOrderStatus(OrderStatus.PAYMENT_FAILED);
            throw PaymentErrorCode.VIRTUAL_ACCOUNT_NOT_ALLOWED.toException();
        }
        if (paymentStatus.equals("cancelled")) {
            orderEntity.changeOrderStatus(OrderStatus.CANCELLED);
            throw PaymentErrorCode.PAYMENT_ALREADY_CANCELLED.toException();
        }

        throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
    }

    public void cancelPayment(OrderEntity orderEntity, String reason) {
        PaymentEntity paymentEntity = paymentQueryService.findPaymentById(orderEntity.getPayment().getId());
        PaymentResponse paymentResponse = paymentApiClient.cancelPayment(paymentEntity.getImpUid(), reason);
        paymentEntity.updateFromPaymentResponse(paymentResponse);
    }

    private void validatePaymentAmount(OrderEntity orderEntity, PaymentResponse paymentResponse) {
        if (!Objects.equals(orderEntity.getTotalPrice().getValue(), paymentResponse.getResponse().getAmount())) {
            orderEntity.changeOrderStatus(OrderStatus.PAYMENT_FAILED);
            cancelPayment(orderEntity, OrderStatus.PAYMENT_FAILED.getDescription());

            throw PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.toException();
        }
    }
}
