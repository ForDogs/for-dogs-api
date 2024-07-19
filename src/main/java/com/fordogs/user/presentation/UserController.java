package com.fordogs.user.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.TokenExtractor;
import com.fordogs.core.util.constants.CookieConstants;
import com.fordogs.core.util.constants.HeaderConstants;
import com.fordogs.core.util.logging.ApiLogging;
import com.fordogs.security.exception.error.SecurityErrorCode;
import com.fordogs.user.application.PasswordResetService;
import com.fordogs.user.application.UserQueryService;
import com.fordogs.user.application.UserService;
import com.fordogs.user.error.PasswordResetErrorCode;
import com.fordogs.user.error.RefreshTokenErrorCode;
import com.fordogs.user.error.UserErrorCode;
import com.fordogs.user.presentation.request.*;
import com.fordogs.user.presentation.response.*;
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

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final PasswordResetService passwordResetService;

    @Operation(summary = "회원 가입", operationId = "/users")
    @ApiErrorCode(UserErrorCode.class)
    @PostMapping
    public ResponseEntity<SuccessResponse<UserSignupResponse>> handleSignupUserRequest(
            @Valid @RequestBody UserSignupRequest request) {
        UserSignupResponse response = userService.signupUser(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "로그인",
            operationId = "/users/login",
            description = "로그인 시 RefreshToken과 UUIDToken은 Set-Cookie 헤더를 통해 응답으로 전달됩니다."
    )
    @ApiLogging
    @ApiErrorCode(UserErrorCode.class)
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handlePerformLoginRequest(
            @Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.performLogin(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "로그아웃",
            operationId = "/users/logout",
            description = "로그아웃 시 클라이언트의 쿠키에 저장되어 있던 UUID 토큰과 Refresh 토큰이 만료되며, 서버에 저장된 해당 Refresh 토큰도 삭제됩니다."
    )
    @ApiLogging
    @ApiErrorCode({RefreshTokenErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<UserLoginResponse>> handlePerformLogoutRequest(
            @Parameter(hidden = true) @CookieValue(value = CookieConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken) {
        userService.performLogout(refreshToken);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "액세스 토큰 재발급",
            operationId = "/users/refresh",
            description = "액세스 토큰이 만료되기 전에 호출하여 리프레쉬 토큰을 통해 새로운 액세스 토큰을 재발급받을 수 있습니다."
    )
    @ApiErrorCode({RefreshTokenErrorCode.class, SecurityErrorCode.class})
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<UserRefreshResponse>> handleRenewAccessTokenRequest(
            @Parameter(hidden = true) @RequestHeader(value = HeaderConstants.AUTHORIZATION_HEADER) String bearerTokenHeader,
            @Parameter(hidden = true) @CookieValue(value = CookieConstants.COOKIE_NAME_REFRESH_TOKEN) String refreshToken,
            @Parameter(hidden = true) @CookieValue(value = CookieConstants.COOKIE_NAME_UUID_TOKEN) String uuidToken) {
        UserRefreshResponse response = userService.renewAccessToken(TokenExtractor.extractAccessToken(bearerTokenHeader), refreshToken, uuidToken);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 탈퇴", operationId = "/users/profile")
    @ApiErrorCode({UserErrorCode.class, SecurityErrorCode.class})
    @DeleteMapping("/profile")
    public ResponseEntity<SuccessResponse<Object>> handleDeactivateUserRequest() {
        userService.deactivateUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "회원 정보 조회", operationId = "/users/profile")
    @ApiErrorCode({UserErrorCode.class, SecurityErrorCode.class})
    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<UserDetailsResponse>> handleFindUserDetailsRequest() {
        UserDetailsResponse response = userQueryService.findUserDetails();

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(
            summary = "비밀번호 초기화 요청",
            operationId = "/users/password-reset",
            description = "이메일 인증 코드 유효 시간은 3분입니다."
    )
    @ApiErrorCode(PasswordResetErrorCode.class)
    @PostMapping("/password-reset")
    public ResponseEntity<SuccessResponse<Object>> handlePasswordResetRequest(
            @Valid @RequestBody UserPasswordResetRequest request) {
        passwordResetService.requestPasswordReset(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "비밀번호 초기화 인증 확인",
            operationId = "/users/password-reset/verify",
            description = "임시 비밀번호 발급 후, 기존 비밀번호로는 로그인할 수 없습니다."
    )
    @ApiErrorCode(PasswordResetErrorCode.class)
    @PostMapping("/password-reset/verify")
    public ResponseEntity<SuccessResponse<UserPasswordResetVerifyResponse>> handlePasswordVerifyRequest(
            @Valid @RequestBody UserPasswordResetVerifyRequest request) {
        UserPasswordResetVerifyResponse response = passwordResetService.verifyPasswordReset(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경", operationId = "/users/password-change")
    @ApiErrorCode({PasswordResetErrorCode.class, SecurityErrorCode.class})
    @PatchMapping("/password-change")
    public ResponseEntity<SuccessResponse<Object>> handlePasswordChangeRequest(
            @Valid @RequestBody UserPasswordChangeRequest request) {
        passwordResetService.changePassword(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "아이디 찾기", operationId = "/users/find-id")
    @ApiErrorCode(UserErrorCode.class)
    @PostMapping("/find-id")
    public ResponseEntity<SuccessResponse<UserFindIdResponse>> handleFindIdRequest(
            @Valid @RequestBody UserFindIdRequest request) {
        UserFindIdResponse response = userQueryService.findUserAccountByNameAndBirthDate(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
