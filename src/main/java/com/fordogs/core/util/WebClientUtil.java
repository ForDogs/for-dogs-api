package com.fordogs.core.util;

import com.fordogs.configuraion.WebClientConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClientConfiguration webClientConfiguration;

    public <T> T get(String url, Class<T> responseDtoClass) {
        return webClientConfiguration.webClient().method(HttpMethod.GET)
                .uri(url)
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }

    public <T> T get(String url, Class<T> responseDtoClass, Consumer<HttpHeaders> headersConsumer) {
        return webClientConfiguration.webClient().method(HttpMethod.GET)
                .uri(url)
                .headers(headersConsumer)
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }

    public <T, V> T post(String url, V requestDto, Class<T> responseDtoClass) {
        return webClientConfiguration.webClient().method(HttpMethod.POST)
                .uri(url)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }

    public <T, V> T post(String url, V requestDto, Class<T> responseDtoClass, Consumer<HttpHeaders> headersConsumer) {
        return webClientConfiguration.webClient().method(HttpMethod.POST)
                .uri(url)
                .headers(headersConsumer)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(responseDtoClass)
                .block();
    }
}
