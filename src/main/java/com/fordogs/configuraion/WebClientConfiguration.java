package com.fordogs.configuraion;

import com.fordogs.core.exception.ExternalApiException;
import com.fordogs.core.exception.error.GlobalErrorCode;
import com.fordogs.payment.application.integration.client.PortOneApiConstants;
import com.fordogs.payment.application.integration.response.PaymentBaseResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(PortOneApiConstants.BASE_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofResponseProcessor(this::renderApiErrorResponse))
                .exchangeStrategies(defaultExchangeStrategies())
                .build();
    }

    @Bean
    public ExchangeStrategies defaultExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    private Mono<ClientResponse> renderApiErrorResponse(ClientResponse clientResponse) {
        if (clientResponse.statusCode().is4xxClientError()) {
            return clientResponse.bodyToMono(PaymentBaseResponse.class)
                    .flatMap(apiErrorResponse -> {
                        String requestUri = clientResponse.request().getURI().toString();
                        String errorMessage = String.format("Error from API [%s]: %s", requestUri, apiErrorResponse.getMessage());

                        return Mono.error(new ExternalApiException(errorMessage, (HttpStatus) clientResponse.statusCode()));
                    });
        }
        if (clientResponse.statusCode().is5xxServerError()) {
            return Mono.error(GlobalErrorCode.INTERNAL_SERVER_ERROR.toException());
        }

        return Mono.just(clientResponse);
    }
}
