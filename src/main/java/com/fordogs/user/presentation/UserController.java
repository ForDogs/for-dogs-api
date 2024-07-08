package com.fordogs.user.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.TokenExtractor;
import com.fordogs.core.util.constants.AuthConstants;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.error.RefreshTokenErrorCode;
import com.fordogs.user.error.UserManagementErrorCode;
import com.fordogs.user.presentation.request.UserLoginRequest;
import com.fordogs.user.presentation.request.UserSignupRequest;
import com.fordogs.user.presentation.response.UserDetailsResponse;
import com.fordogs.user.presentation.response.UserLoginResponse;
import com.fordogs.user.presentation.response.UserRefreshResponse;
import com.fordogs.user.presentation.response.UserSignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

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
            description = "로그인 시 RefreshToken과 UUIDToken은 Set-Cookie 헤더를 통해 응답으로 전달됩니다."
    )
    @ApiErrorCode(UserManagementErrorCode.class)
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handlePerformLoginRequest(
            @Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userManagementService.performLogin(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "로그아웃",
            operationId = "/users/logout",
            description = "해당 API는 Swagger UI에서 테스트할 수 없습니다."
    )
    @ApiErrorCode({RefreshTokenErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handlePerformLogoutRequest(
            @Parameter(hidden = true) @CookieValue(value = CookieConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken) {
        userManagementService.performLogout(refreshToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "액세스 토큰 재발급",
            operationId = "/users/refresh",
            description = "해당 API는 Swagger UI에서 테스트할 수 없습니다."
    )
    @ApiErrorCode({RefreshTokenErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<UserRefreshResponse>> handleRenewAccessTokenRequest(
            @RequestHeader(value = AuthConstants.AUTHORIZATION_HEADER) String bearerTokenHeader,
            @CookieValue(value = CookieConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken,
            @CookieValue(value = CookieConstants.COOKIE_NAME_UUID_TOKEN) String uuidToken) {
        UserRefreshResponse response = userManagementService.renewAccessToken(TokenExtractor.extractAccessToken(bearerTokenHeader), refreshToken, uuidToken);

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
