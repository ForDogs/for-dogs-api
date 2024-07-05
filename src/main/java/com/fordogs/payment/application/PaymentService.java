package com.fordogs.payment.application;


import com.fordogs.core.exception.DomainException;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.order.application.OrderService;
import com.fordogs.order.domain.entity.OrderEntity;
import com.fordogs.order.domain.eums.OrderStatus;
import com.fordogs.payment.application.integration.client.PaymentApiClient;
import com.fordogs.payment.application.integration.response.PaymentDetailResponse;
import com.fordogs.payment.application.integration.response.PaymentTokenResponse;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final PaymentApiClient paymentApiClient;
    private final PaymentRepository paymentRepository;

    @Transactional(noRollbackFor = DomainException.class)
    public PaymentCompleteResponse paymentComplete(PaymentCompleteRequest request) {
        PaymentTokenResponse tokenResponse = paymentApiClient.getAccessToken();
        PaymentDetailResponse paymentDetailResponse = paymentApiClient.getPaymentDetail(request.getImpUid(), tokenResponse.getResponse().getAccessToken());

        OrderEntity orderEntity = orderService.findOrderById(request.getMerchantUid());
        validatePaymentAmount(orderEntity, paymentDetailResponse);

        String paymentStatus = paymentDetailResponse.getResponse().getStatus();
        if (paymentStatus.equals("paid")) {
            PaymentEntity savedPaymentEntity = paymentRepository.save(paymentDetailResponse.toEntity(orderEntity));
            orderEntity.changeOrderStatus(OrderStatus.PAID);
            return PaymentCompleteResponse.toResponse(savedPaymentEntity);
        }
        if (paymentStatus.equals("ready")) {
            orderEntity.changeOrderStatus(OrderStatus.PAYMENT_FAILED);
            throw PaymentErrorCode.VIRTUAL_ACCOUNT_NOT_ALLOWED.toException();
        }

        orderEntity.changeOrderStatus(OrderStatus.PAYMENT_FAILED);
        throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
    }

    private void validatePaymentAmount(OrderEntity orderEntity, PaymentDetailResponse paymentDetailResponse) {
        if (!Objects.equals(orderEntity.getTotalPrice().getValue(), paymentDetailResponse.getResponse().getAmount())) {
            orderEntity.changeOrderStatus(OrderStatus.CANCELLED);
            // TODO: 결제 취소 로직 호출
            throw PaymentErrorCode.PAYMENT_AMOUNT_MISMATCH.toException();
        }
    }
}
