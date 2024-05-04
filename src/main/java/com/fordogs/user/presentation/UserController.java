package com.fordogs.user.presentation;

import com.fordogs.core.presentation.SuccessResponse;
import com.fordogs.user.application.UserService;
import com.fordogs.user.presentation.dto.JoinDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "User", description = "User APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", operationId = "/users/join")
    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<Object>> handleJoinUserRequest(@Valid @RequestBody JoinDto.Request request) {
        JoinDto.Response response = userService.joinUser(request);

        return ResponseEntity.created(URI.create(""))
                .body(SuccessResponse.of(response));
    }
}
