package com.fordogs.user.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.HttpTokenExtractor;
import com.fordogs.core.util.constants.HttpResponseConstants;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.user.application.UserAuthService;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.application.UserRefreshTokenService;
import com.fordogs.user.error.UserManagementErrorCode;
import com.fordogs.user.error.UserRefreshTokenErrorCode;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserLoginResponse;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;
    private final UserRefreshTokenService userRefreshTokenService;
    private final UserAuthService userAuthService;

    @Operation(summary = "회원 가입", operationId = "/users/signup")
    @ApiErrorCode(UserManagementErrorCode.class)
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<UserSignupResponse>> handleSignupUserRequest(
            @Valid @RequestBody UserSignupRequest request) {
        UserSignupResponse response = userManagementService.signupUser(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "로그인",
            operationId = "/users/login",
            description = "로그인 시 Refresh Token은 Set-Cookie 헤더를 통해 응답으로 전달됩니다."
    )
    @ApiErrorCode(UserManagementErrorCode.class)
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handleLoginRequest(
            @Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userAuthService.login(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "액세스 토큰 재발급",
            operationId = "/users/refresh",
            description = "해당 API는 Swagger UI에서 테스트할 수 없습니다. " +
                          "요청 헤더에서 Bearer 토큰과 쿠키에서 Refresh 토큰을 사용합니다."
    )
    @ApiErrorCode(UserRefreshTokenErrorCode.class)
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<UserRefreshResponse>> handleRefreshAccessTokenRequest(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerTokenHeader,
            @CookieValue(value = HttpResponseConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken) {
        UserRefreshResponse response = userRefreshTokenService.refreshAccessToken(HttpTokenExtractor.extractAccessToken(bearerTokenHeader), refreshToken);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 탈퇴", operationId = "/users/deactivate")
    @ApiErrorCode({UserManagementErrorCode.class, SecurityErrorCode.class})
    @DeleteMapping("/deactivate")
    public ResponseEntity<SuccessResponse<Object>> handleDeactivateUserRequest() {
        userManagementService.deactivateUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "회원 정보 조회", operationId = "/users/details")
    @ApiErrorCode({UserManagementErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<UserDetailsResponse>> handleFindUserDetailsRequest() {
        UserDetailsResponse response = userManagementService.findUserDetails();

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
