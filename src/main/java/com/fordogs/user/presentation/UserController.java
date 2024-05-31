package com.fordogs.user.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.HeaderUtil;
import com.fordogs.core.util.constants.HttpRequestConstants;
import com.fordogs.security.exception.error.SecurityErrorCode;
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

    @Operation(summary = "회원 가입", operationId = "/users/signup")
    @ApiErrorCode(UserManagementErrorCode.class)
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<UserSignupResponse>> handleSignupUserRequest(
            @Valid @RequestBody UserSignupRequest request) {
        UserSignupResponse response = userManagementService.signupUser(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", operationId = "/users/login")
    @ApiErrorCode(UserManagementErrorCode.class)
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handleLoginRequest(
            @Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userManagementService.login(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "해당 API는 Swagger UI에서 테스트 불가합니다.", operationId = "/users/refresh")
    @ApiErrorCode(UserRefreshTokenErrorCode.class)
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<UserRefreshResponse>> handleRefreshAccessTokenRequest(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerTokenHeader,
            @CookieValue(value = HttpRequestConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken) {
        UserRefreshResponse response = userRefreshTokenService.refreshAccessToken(HeaderUtil.extractAccessToken(bearerTokenHeader), refreshToken);

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
