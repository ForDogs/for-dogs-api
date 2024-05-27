package com.fordogs.user.presentation;

import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.core.exception.error.UserRefreshTokenServiceErrorCode;
import com.fordogs.core.exception.error.SecurityServiceErrorCode;
import com.fordogs.core.exception.error.UserManagementServiceErrorCode;
import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.HeaderUtil;
import com.fordogs.user.application.UserManagementService;
import com.fordogs.user.application.UserRefreshTokenService;
import com.fordogs.user.presentation.dto.UserDetailDto;
import com.fordogs.user.presentation.dto.UserJoinDto;
import com.fordogs.user.presentation.dto.UserLoginDto;
import com.fordogs.user.presentation.dto.UserRefreshTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    private final UserRefreshTokenService userRefreshTokenService;

    @Operation(summary = "회원 가입", operationId = "/users/join")
    @ApiErrorCode(UserManagementServiceErrorCode.class)
    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<UserJoinDto.Response>> handleJoinUserRequest(
            @Valid @RequestBody UserJoinDto.Request request) {
        UserJoinDto.Response response = userManagementService.joinUser(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", operationId = "/users/login")
    @ApiErrorCode(UserManagementServiceErrorCode.class)
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<UserLoginDto.Response>> handleLoginRequest(
            @Valid @RequestBody UserLoginDto.Request request) {
        UserLoginDto.Response response = userManagementService.login(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "요청 시 HTTP Cookie에 RefreshToken이 존재해야 합니다. [EX] REFRESH_TOKEN={VALUE}", operationId = "/users/refresh-token")
    @ApiErrorCode(UserRefreshTokenServiceErrorCode.class)
    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<UserRefreshTokenDto.Response>> handleRefreshAccessTokenRequest(
            @Parameter(name = "AccessToken", required = true, in = ParameterIn.HEADER) @RequestHeader(name = "Authorization") String authorizationHeader) {
        UserRefreshTokenDto.Response response = userRefreshTokenService.refreshAccessToken(HeaderUtil.extractAccessToken(authorizationHeader));

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 탈퇴", operationId = "/users/deactivation")
    @ApiErrorCode({UserManagementServiceErrorCode.class, SecurityServiceErrorCode.class})
    @DeleteMapping("/deactivation")
    public ResponseEntity<SuccessResponse<Object>> handleDeactivateUserRequest() {
        userManagementService.deactivateUser();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "회원 정보 조회", operationId = "/users/profile")
    @ApiErrorCode({UserManagementServiceErrorCode.class, SecurityServiceErrorCode.class})
    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<UserDetailDto.Response>> handleFindUserDetailsRequest() {
        UserDetailDto.Response response = userManagementService.findUserDetails();

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.OK);
    }
}
