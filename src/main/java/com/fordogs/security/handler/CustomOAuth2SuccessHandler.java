package com.fordogs.security.handler;

import com.fordogs.core.util.CookieUtil;
import com.fordogs.core.util.StringGenerator;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.core.util.crypto.EncryptionUtil;
import com.fordogs.security.application.dto.CustomOAuth2User;
import com.fordogs.security.exception.error.OAuth2ErrorCode;
import com.fordogs.security.infrastructure.CustomAuthorizationRequestRepository;
import com.fordogs.user.domain.entity.UserEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.fordogs.security.infrastructure.CustomAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${service.domain}")
    public String serviceDomain;

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    private static final long AUTH_CODE_EXPIRATION_TIME_MILLIS = 180_000;
    private static final String AUTH_CODE_PARAM = "code";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        handleSuccessfulAuthentication(request, response, customOAuth2User.userEntity());
        customAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private void handleSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, UserEntity userEntity) throws IOException {
        String redirectUri = getRedirectUriFromCookie(request);
        if (!isDomainIncluded(redirectUri)) {
            throw OAuth2ErrorCode.UNAUTHORIZED_REDIRECT_URL.toException();
        }

        String authCode = createAuthCode(userEntity);
        String targetUrl = buildRedirectUrl(redirectUri, authCode);
        response.sendRedirect(targetUrl);
    }

    private boolean isDomainIncluded(String redirectUri) {
        try {
            URI uri = new URI(redirectUri);
            String host = uri.getHost();
            return host != null && host.endsWith(serviceDomain);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private String createAuthCode(UserEntity userEntity) {
        String userId = userEntity.getId().toString();
        String provider = userEntity.getProvider().name();
        long authCodeExpirationTime = System.currentTimeMillis() + AUTH_CODE_EXPIRATION_TIME_MILLIS;
        UUID uniqueIdentifier = StringGenerator.generateSequentialUUID();

        Map<String, Object> authCodeData = new HashMap<>();
        authCodeData.put("userId", userId);
        authCodeData.put("provider", provider);
        authCodeData.put("authCodeExpirationTime", authCodeExpirationTime);
        authCodeData.put("uniqueIdentifier", uniqueIdentifier.toString());

        return EncryptionUtil.encrypt(JsonConverter.convertObjectToJson(authCodeData));
    }

    private String getRedirectUriFromCookie(HttpServletRequest request) {
        return CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElseThrow(OAuth2ErrorCode.INVALID_REDIRECT_URL::toException);
    }

    private String buildRedirectUrl(String redirectUri, String encryptedAuthCode) {
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam(AUTH_CODE_PARAM, encryptedAuthCode)
                .build()
                .toUriString();
    }
}
