package com.fordogs.payment.application.integration.client;

import com.fordogs.configuraion.properties.PortOneProperties;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.core.util.constants.PortOneApiConstants;
import com.fordogs.payment.application.integration.response.PaymentDetailResponse;
import com.fordogs.payment.application.integration.response.PaymentTokenResponse;
import com.fordogs.core.util.api.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentApiClient {

    private final WebClientUtil webClientUtil;
    private final PortOneProperties portOneProperties;

    private static final String TOKEN_REQUEST_KEY = "imp_key";
    private static final String TOKEN_REQUEST_SECRET = "imp_secret";

    public PaymentTokenResponse getAccessToken() {
        Map<String, String> requestBody = createTokenRequestBody();

        PaymentTokenResponse tokenResponse = webClientUtil.post(
                PortOneApiConstants.ENDPOINT_GET_TOKEN,
                requestBody,
                PaymentTokenResponse.class
        );

        if (tokenResponse == null || tokenResponse.getResponse().getAccessToken() == null) {
            throw GlobalErrorCode.INTERNAL_SERVER_ERROR.toException();
        }

        return tokenResponse;
    }

    public PaymentDetailResponse getPaymentDetail(String impUid, String accessToken) {
        String url = String.format("%s/%s", PortOneApiConstants.ENDPOINT_GET_PAYMENT, impUid);

        return webClientUtil.get(
                url,
                PaymentDetailResponse.class,
                HttpHeaders.AUTHORIZATION, "Bearer " + accessToken
        );
    }

    private Map<String, String> createTokenRequestBody() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put(TOKEN_REQUEST_KEY, portOneProperties.getApiKey());
        requestBody.put(TOKEN_REQUEST_SECRET, portOneProperties.getApiSecret());

        return requestBody;
    }
}
