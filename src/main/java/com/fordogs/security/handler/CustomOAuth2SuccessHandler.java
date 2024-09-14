package com.fordogs.security.handler;

import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.converter.JsonConverter;
import com.fordogs.security.application.dto.CustomOAuth2User;
import com.fordogs.user.application.UserService;
import com.fordogs.user.presentation.response.UserLoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        UserLoginResponse userLoginResponse = userService.performSocialLogin(customOAuth2User.userEntity());

        sendJsonResponse(response, SuccessResponse.of(userLoginResponse));
    }

    private void sendJsonResponse(HttpServletResponse response, SuccessResponse<UserLoginResponse> userLoginResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_CREATED);
        String jsonResponse = JsonConverter.convertObjectToJson(userLoginResponse);
        response.getWriter().write(jsonResponse);
    }
}
