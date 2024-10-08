package com.fordogs.payment.application.portone.client;

import com.fordogs.configuraion.properties.PortOneProperties;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.WebClientUtil;
import com.fordogs.payment.application.portone.response.PaymentResponse;
import com.fordogs.payment.application.portone.response.PaymentTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {

    private final WebClientUtil webClientUtil;
    private final PortOneProperties portOneProperties;

    public PaymentResponse getPaymentDetail(String impUid) {
        String accessToken = getAccessToken().getResponse().getAccessToken();
        String url = String.format("%s/%s", PortOneApiConstants.ENDPOINT_GET_PAYMENT, impUid);

        return webClientUtil.get(
                url,
                PaymentResponse.class,
                headers -> headers.setBearerAuth(accessToken)
        );
    }

    public PaymentResponse cancelPayment(String impUid, String reason) {
        String accessToken = getAccessToken().getResponse().getAccessToken();
        String url = PortOneApiConstants.ENDPOINT_CANCEL_PAYMENT;
        Map<String, String> requestBody = buildCancelPaymentRequestBody(impUid, reason);

        return webClientUtil.post(
                url,
                requestBody,
                PaymentResponse.class,
                headers -> headers.setBearerAuth(accessToken)
        );
    }

    private PaymentTokenResponse getAccessToken() {
        String url = PortOneApiConstants.ENDPOINT_GET_TOKEN;
        Map<String, String> requestBody = buildTokenRequestBody();

        PaymentTokenResponse tokenResponse = webClientUtil.post(
                url,
                requestBody,
                PaymentTokenResponse.class
        );

        if (tokenResponse == null || tokenResponse.getResponse().getAccessToken() == null) {
            throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
        }

        return tokenResponse;
    }

    private Map<String, String> buildCancelPaymentRequestBody(String impUid, String reason) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("imp_uid", impUid);
        requestBody.put("reason", reason);

        return requestBody;
    }

    private Map<String, String> buildTokenRequestBody() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("imp_key", portOneProperties.getApiKey());
        requestBody.put("imp_secret", portOneProperties.getApiSecret());

        return requestBody;
    }
}
