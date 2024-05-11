package com.fordogs.user.presentation;

import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.core.util.HeaderUtil;
import com.fordogs.user.application.RefreshTokenService;
import com.fordogs.user.application.UserService;
import com.fordogs.user.presentation.dto.JoinDto;
import com.fordogs.user.presentation.dto.LoginDto;
import com.fordogs.user.presentation.dto.RefreshTokenDto;
import io.swagger.v3.oas.annotations.Operation;
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
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "회원 가입", operationId = "/users/join")
    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<JoinDto.Response>> handleJoinUserRequest(@Valid @RequestBody JoinDto.Request request) {
        JoinDto.Response response = userService.joinUser(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "로그인", operationId = "/users/login")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginDto.Response>> handleLoginRequest(@Valid @RequestBody LoginDto.Request request) {
        LoginDto.Response response = userService.login(request);

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }

    @Operation(summary = "액세스 토큰 재발급", operationId = "/users/refresh-token")
    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<RefreshTokenDto.Response>> handleRefreshAccessTokenRequest(@RequestHeader(name = "Authorization") String authorizationHeader) {
        RefreshTokenDto.Response response = refreshTokenService.refreshAccessToken(HeaderUtil.extractAccessToken(authorizationHeader));

        return new ResponseEntity<>(SuccessResponse.of(response), HttpStatus.CREATED);
    }
}
