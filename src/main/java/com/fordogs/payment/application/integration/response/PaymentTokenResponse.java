package com.fordogs.payment.application.integration.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PaymentTokenResponse extends PaymentBaseResponse {

    private AuthResponse response;

    @Getter
    @Setter
    @ToString
    public static class AuthResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expired_at")
        private Integer expiredAt;

        private Integer now;
    }
}
